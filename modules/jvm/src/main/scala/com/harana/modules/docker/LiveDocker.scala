package com.harana.modules.docker

import java.io.{Closeable, File, InputStream}
import com.github.dockerjava.api.async.ResultCallback
import com.github.dockerjava.api.command._
import com.github.dockerjava.api.exception.{DockerException, NotFoundException, UnauthorizedException}
import com.github.dockerjava.api.model.Network.Ipam
import com.github.dockerjava.api.model.Ports.Binding
import com.github.dockerjava.api.model.{Service => DockerService, _}
import com.github.dockerjava.core.{DefaultDockerClientConfig, DockerClientBuilder}
import com.github.dockerjava.okhttp.OkHttpDockerCmdExecFactory
import com.github.dockerjava.api.model.HostConfig.newHostConfig
import com.harana.modules.core.config.Config
import com.harana.modules.docker.Docker._
import com.harana.modules.core.logger.Logger
import com.harana.modules.core.okhttp.OkHttp
import com.harana.modules.core.okhttp.models.OkHttpError
import zio.blocking.Blocking
import zio.{Has, IO, Queue, UIO, ZLayer}
import io.circe.parser._
import org.json4s.DefaultFormats

import scala.jdk.CollectionConverters._

object LiveDocker {
  val layer = ZLayer.fromServices { (blocking: Blocking.Service,
                                     config: Config.Service,
                                     logger: Logger.Service,
                                     okHttp: OkHttp.Service) => new Docker.Service {

    private implicit val formats = DefaultFormats
    private val registryImage = "registry:latest"

    private val client = for {
      dockerHost        <- config.string("docker.host", "127.0.0.1")
      dockerPort        <- config.int("docker.port", 1234)
      tlsVerify         <- config.boolean("docker.tlsVerify", default = false)
      certPath          <- config.optString("docker.certPath")
      registryUsername  <- config.optSecret("docker-registry-username")
      registryPassword  <- config.optSecret("docker-registry-password")
      registryEmail     <- config.optString("docker.registryEmail")
      registryUrl       <- config.optString("docker.registryUrl")
    } yield {
      val config = DefaultDockerClientConfig.createDefaultConfigBuilder()
        .withDockerHost("unix:///var/run/docker.sock")
        //.withDockerHost(s"tcp://$dockerHost:$dockerPort")
        .withDockerTlsVerify(tlsVerify)

      if (certPath.isDefined) config.withDockerCertPath(certPath.get)
      if (registryUsername.isDefined) config.withRegistryUsername(registryUsername.get)
      if (registryPassword.isDefined) config.withRegistryPassword(registryPassword.get)
      if (registryEmail.isDefined) config.withRegistryEmail(registryEmail.get)
      if (registryUrl.isDefined) config.withRegistryUrl(registryUrl.get)

      DockerClientBuilder.getInstance(config.build())
        .withDockerCmdExecFactory(new OkHttpDockerCmdExecFactory()).build()
    }


    def attachContainer(id: ContainerId): IO[DockerException, InputStream] =
      client.map(_.attachContainerCmd(id).getStdin)


    def auth(username: Option[String], password: Option[String], identityToken: Option[String], registryToken: Option[String]): IO[UnauthorizedException, String] =
      for {
        authConfig <- authConfig(username, password, identityToken, registryToken)
        auth <- auth(Some(authConfig))
      } yield auth


    def auth(config: Option[AuthConfig]): IO[UnauthorizedException, String] =
      client.map { c =>
        val cmd = c.authCmd
        if (config.isDefined) cmd.withAuthConfig(config.get)
        cmd.exec().getIdentityToken
      }


    def authConfig(username: Option[String], password: Option[String], identityToken: Option[String], registryToken: Option[String]): UIO[AuthConfig] =
      (username, password, identityToken, registryToken) match {
        case (Some(u), Some(p), _, _) => UIO(new AuthConfig().withUsername(u).withPassword(p))
        case (_, _, Some(it), _) => UIO(new AuthConfig().withIdentityToken(it))
        case (_, _, _, Some(rt)) => UIO(new AuthConfig().withRegistrytoken(rt))
        case (_, _, _, _) => UIO(new AuthConfig())
      }


    def buildImage(dockerFileOrFolder: File, tags: Set[String]): IO[DockerException, ImageId] =
      client.flatMap { c =>
        IO.effectAsync { cb =>
          c.buildImageCmd(dockerFileOrFolder).withTags(tags.asJava).exec(
            new BuildImageResultCallback() {
              override def onNext(item: BuildResponseItem): Unit = cb(IO.succeed(item.getImageId))
              override def onError(throwable: Throwable): Unit = cb(IO.fail(throwable.asInstanceOf[DockerException]))
            }
          )
        }
      }


    def buildImage(inputStream: InputStream, tags: Set[String]): IO[DockerException, ImageId] =
      client.flatMap { c =>
        IO.effectAsync { cb =>
          c.buildImageCmd(inputStream).withTags(tags.asJava).exec(
            new BuildImageResultCallback() {
              override def onNext(item: BuildResponseItem): Unit = cb(IO.succeed(item.getImageId))
              override def onError(throwable: Throwable): Unit = cb(IO.fail(throwable.asInstanceOf[DockerException]))
            }
          )
        }
      }

    def commit(id: ContainerId): IO[NotFoundException, String] =
      client.map(_.commitCmd(id).exec())


    def connectToNetwork: UIO[Unit] =
      client.map(_.connectToNetworkCmd().exec())


    def containerDiff(id: ContainerId): IO[NotFoundException, List[ChangeLog]] =
      client.map(_.containerDiffCmd(id).exec().asScala.toList)


    def containerExists(containerName: String): UIO[Boolean] =
      listContainers(filters = Map("name" -> List(containerName))).map(_.nonEmpty)


    def containerNotExists(containerName: String): UIO[Boolean] =
      containerExists(containerName).map(result => !result)


    def containerRunning(containerName: String): UIO[Boolean] =
      listContainers(filters = Map("name" -> List(containerName), "status" -> List("running"))).map(_.nonEmpty)


    def containerNotRunning(containerName: String): UIO[Boolean] =
      containerRunning(containerName).map(result => !result)


    def copyResourceFromContainer(id: ContainerId, resource: String, hostPath: Option[String] = None): IO[NotFoundException, InputStream] =
      client.map { c =>
        val cmd = c.copyArchiveFromContainerCmd(id, resource)
        if (hostPath.isDefined) cmd.withHostPath(hostPath.get)
        cmd.exec()
      }


    def copyArchiveToContainer(id: ContainerId, tarInputStream: InputStream, remotePath: Option[String] = None): IO[NotFoundException, Unit] =
      client.map { c =>
        val cmd = c.copyArchiveToContainerCmd(id).withTarInputStream(tarInputStream)
        if (remotePath.isDefined) cmd.withRemotePath(remotePath.get)
        cmd.exec()
      }


    def copyResourceToContainer(id: ContainerId, resource: String, remotePath: Option[String] = None): IO[NotFoundException, Unit] =
      client.map { c =>
        val cmd = c.copyArchiveToContainerCmd(id).withHostResource(resource)
        if (remotePath.isDefined) cmd.withRemotePath(remotePath.get)
        cmd.exec()
      }



    def createContainer(name: String,
                        imageName: String,
                        command: Option[String] = None,
                        exposedPorts: Map[Int, Int] = Map()): IO[DockerException, ContainerId] = {
      val bindings = new Ports()
      exposedPorts.foreach { case (k, v) => bindings.bind(new ExposedPort(k), Binding.bindPort(v)) }

      client.map(_.createContainerCmd(imageName).withName(name)
        .withHostConfig(newHostConfig().withPortBindings(bindings))
        .withExposedPorts(exposedPorts.keys.map(new ExposedPort(_)).toList.asJava)
        .exec().getId
      )
    }


    def createImage(repository: String, imageStream: InputStream): IO[NotFoundException, ImageId] =
      client.map(_.createImageCmd(repository, imageStream).exec().getId)


    def createNetwork(name: Option[String] = None,
                      attachable: Boolean = false,
                      checkDuplicate: Boolean = false,
                      driver: Option[String] = None,
                      enableIpv6: Boolean = false,
                      internal: Boolean = false,
                      ipam: Option[Ipam] = None,
                      labels: Map[String, String] = Map(),
                      options: Map[String, String] = Map()): IO[DockerException, NetworkId] =
      client.map { c =>
        val cmd = c.createNetworkCmd
          .withAttachable(attachable)
          .withCheckDuplicate(checkDuplicate)
          .withEnableIpv6(enableIpv6)
          .withInternal(internal)
          .withLabels(labels.asJava)
          .withOptions(options.asJava)
        if (ipam.isDefined) cmd.withIpam(ipam.get)
        if (name.isDefined) cmd.withName(name.get)
        cmd.exec().getId
      }


    def createService(spec: ServiceSpec): IO[NotFoundException, ServiceId] =
      client.map(_.createServiceCmd(spec).exec().getId)


    def createVolume(name: String, driver: Option[String] = None, driverOpts: Map[String, String] = Map()): IO[NotFoundException, String] =
      client.map { c =>
        val cmd = c.createVolumeCmd.withName(name).withDriverOpts(driverOpts.asJava)
        if (driver.isDefined) cmd.withDriver(driver.get)
        cmd.exec().getMountpoint
      }


    def disconnectFromNetwork(networkId: Option[NetworkId] = None, containerId: Option[ContainerId] = None, force: Boolean = false): UIO[Unit] =
      client.map { c =>
        val cmd = c.disconnectFromNetworkCmd().withForce(force)
        if (containerId.isDefined) cmd.withContainerId(containerId.get)
        if (networkId.isDefined) cmd.withNetworkId(networkId.get)
        cmd.exec()
      }


    def ensureContainerIsRunning(name: String, imageName: String, command: Option[String] = None, exposedPorts: Map[Int, Int] = Map()): IO[DockerException, Unit] =
      for {
        running     <- containerRunning(name)
        existing    <- listContainers(nameFilter = List(name), showAll = Some(true ))
        _           <- IO.when(!running)(logger.debug(s"$name not running, starting ${if (existing.isEmpty) "new" else "existing"} container."))
        _           <- IO.when(!running && existing.isEmpty)(
          for {
            _   <- pullImage(imageName)
            id  <- createContainer(name, imageName, command, exposedPorts)
            _   <- startContainer(id)
          } yield ()
        )
        _           <- IO.when(!running && existing.nonEmpty)(startContainer(existing.head.getId))
      } yield ()


    def ensureLocalRegistryIsRunning: IO[DockerException, Unit] =
      ensureContainerIsRunning("registry", registryImage, exposedPorts = Map(5000 -> 5000))


    def events(containerFilter: List[ContainerId] = List(),
               eventFilter: List[EventId] = List(),
               imageFilter: List[ImageId] = List(),
               labelFilter: Map[String, String] = Map(),
               withSince: Option[String] = None,
               withUntil: Option[String] = None): UIO[Queue[Event]] =
      for {
        c <- client
        q <- Queue.unbounded[Event]
      } yield {
        val cmd = c.eventsCmd()
          .withContainerFilter(containerFilter: _*)
          .withEventFilter(eventFilter: _*)
          .withImageFilter(imageFilter: _*)
          .withLabelFilter(labelFilter.asJava)
        if (withSince.isDefined) cmd.withSince(withSince.get)
        if (withUntil.isDefined) cmd.withUntil(withUntil.get)

        cmd.exec(
          new ResultCallback[Event]() {
            override def onNext(item: Event): Unit = q.offer(item)
            override def onError(throwable: Throwable): Unit = {}
            override def onStart(closeable: Closeable): Unit = {}
            override def onComplete(): Unit = {}
            override def close(): Unit = {}
          }
        )
        q
      }


    def execCreate(id: ExecId,
                   attachStderr: Boolean = false,
                   attachStdin: Boolean = false,
                   attachStdout: Boolean = false,
                   cmd: List[String] = List(),
                   containerId: Option[ContainerId] = None,
                   env: List[String] = List(),
                   privileged: Boolean = false,
                   tty: Boolean = false,
                   user: Option[String] = None,
                   workingDir: Option[String] = None): IO[NotFoundException, String] =
      client.map { c =>
        var execCreateCmd = c.execCreateCmd(id)
          .withAttachStderr(attachStderr)
          .withAttachStdin(attachStdin)
          .withAttachStdout(attachStdout)
          .withCmd(cmd: _*)
          .withEnv(env.asJava)
          .withPrivileged(privileged)
          .withTty(tty)
        if (containerId.isDefined) execCreateCmd = execCreateCmd.withContainerId(containerId.get)
        if (user.isDefined) execCreateCmd = execCreateCmd.withUser(user.get)
        execCreateCmd.exec().getId
      }


    def execStart(id: ExecId,
                  detach: Boolean = false,
                  stdIn: Option[InputStream] = None,
                  tty: Boolean = false): UIO[Queue[Frame]] =
      for {
        c <- client
        q <- Queue.unbounded[Frame]
      } yield {
        val cmd = c.execStartCmd(id).withDetach(detach).withTty(tty)
        if (stdIn.isDefined) cmd.withStdIn(stdIn.get)
        cmd.exec(
          new ResultCallback[Frame]() {
            override def onNext(item: Frame): Unit = q.offer(item)
            override def onError(throwable: Throwable): Unit = {}
            override def onStart(closeable: Closeable): Unit = {}
            override def onComplete(): Unit = {}
            override def close(): Unit = {}
          }
        )
        q
      }


    def info: UIO[Info] =
      client.map(_.infoCmd().exec())


    def initializeSwarm(spec: SwarmSpec): UIO[Unit] =
      client.map(_.initializeSwarmCmd(spec).exec())


    def inspectContainer(id: ContainerId): IO[NotFoundException, InspectContainerResponse] =
      client.map(_.inspectContainerCmd(id).exec())


    def inspectExec(id: ExecId): IO[NotFoundException, InspectExecResponse] =
      client.map(_.inspectExecCmd(id).exec())


    def inspectImage(id: ImageId): IO[NotFoundException, InspectImageResponse] =
      client.map(_.inspectImageCmd(id).exec())


    def inspectNetwork(id: Option[NetworkId] = None): IO[NotFoundException, Network] =
      client.map(_.inspectNetworkCmd().exec())


    def inspectService(id: ServiceId): IO[NotFoundException, DockerService] =
      client.map(_.inspectServiceCmd(id).exec())


    def inspectSwarm: IO[NotFoundException, Swarm] =
      client.map(_.inspectSwarmCmd().exec())


    def inspectVolume(name: String): IO[NotFoundException, InspectVolumeResponse] =
      client.map(_.inspectVolumeCmd(name).exec())


    def joinSwarm(advertiseAddr: Option[String] = None,
                  joinToken: Option[String] = None,
                  listenAddr: Option[String] = None,
                  remoteAddrs: List[String] = List()): UIO[Unit] =
      client.map { c =>
        val cmd = c.joinSwarmCmd().withRemoteAddrs(remoteAddrs.asJava)
        if (advertiseAddr.isDefined) cmd.withAdvertiseAddr(advertiseAddr.get)
        if (joinToken.isDefined) cmd.withJoinToken(joinToken.get)
        if (listenAddr.isDefined) cmd.withListenAddr(listenAddr.get)
        cmd.exec()
      }


    def killContainer(id: ContainerId, signal: Option[String] = None): IO[NotFoundException, Unit] =
      client.map { c =>
        val cmd = c.killContainerCmd(id)
        if (signal.isDefined) cmd.withSignal(signal.get)
        cmd.exec()
      }


    //    def launchListenContainer: IO[DockerException, List[String]] =
    //      for {
    //        cmd <- Command(
    //          "docker", "run", "-d", "-v", "/var/run/docker.sock:/var/run/docker.sock", "-p", "127.0.0.1:1234:1234",
    //          "bobrik/socat", "TCP-LISTEN:1234,fork", "UNIX-CONNECT:/var/run/docker.sock").lines.provide(Has(blocking))
    //      } yield cmd


    def leaveSwarm(force: Boolean = false): UIO[Unit] =
      client.map(_.leaveSwarmCmd().withForceEnabled(force).exec())


    def listArtifactoryRepositories(registryUrl: String,
                                    repository: String,
                                    authConfig: AuthConfig): IO[OkHttpError, List[String]] =
null
//      for {
//        token             <- okHttp.getAsJson(s"$registryUrl/v2/token").map(_.hcursor.downField("token").as[String])
//        headers           =  Map("Authorization" -> s"Bearer $token")
//        url               =  s"$registryUrl/api/docker/$repository/v2/_catalog"
//        repositoriesJson  <- okHttp.getAsJson(url, Map(), headers).map(_.hcursor.downField("repositories").as[String])
//        repositories      =  repositoriesJson.children.map(_.extract[String])
//      } yield repositories


    def listArtifactoryTags(registryUrl: String,
                            repository: String,
                            authConfig: AuthConfig,
                            image: String,
                            maximum: Option[Int] = None): IO[OkHttpError, List[String]] =
  null
//      for {
//        token         <- okHttp.getAsJson(s"$registryUrl/v2/token").map(_.hcursor.downField("token").as[String])
//        headers       =  Map("Authorization" -> s"Bearer $token")
//        url           =  s"$registryUrl/api/docker/$repository/v2/${image}/tags/list?n=${maximum.getOrElse(5)}"
//        repositories  <- okHttp.getAsJson(url, Map(), headers).map(_.hcursor.downField("tags").as[List[String]])
//      } yield repositories


    def listContainers(ancestorFilter: List[String] = List(),
                       before: Option[String] = None,
                       exitedFilter: Option[Int] = None,
                       filters: Map[String, List[String]] = Map(),
                       idFilter: List[String] = List(),
                       labelFilter: Map[String, String] = Map(),
                       limit: Option[Int] = None,
                       nameFilter: List[String] = List(),
                       networkFilter: List[String] = List(),
                       showAll: Option[Boolean] = None,
                       showSize: Option[Boolean] = None,
                       since: Option[String] = None,
                       statusFilter: List[String] = List(),
                       volumeFilter: List[String] = List()): UIO[List[Container]] =
      client.map { c =>
        val cmd = c.listContainersCmd
        if (ancestorFilter.nonEmpty) cmd.withAncestorFilter(ancestorFilter.asJava)
        if (filters.nonEmpty) filters.foreach { case (k, v) => cmd.getFilters.put(k, v.asJava) }
        if (idFilter.nonEmpty) cmd.withIdFilter(idFilter.asJava)
        if (labelFilter.nonEmpty) cmd.withLabelFilter(labelFilter.asJava)
        if (nameFilter.nonEmpty) cmd.withNameFilter(nameFilter.asJava)
        if (networkFilter.nonEmpty) cmd.withNetworkFilter(networkFilter.asJava)
        if (statusFilter.nonEmpty) cmd.withStatusFilter(statusFilter.asJava)
        if (volumeFilter.nonEmpty) cmd.withVolumeFilter(volumeFilter.asJava)
        if (before.isDefined) cmd.withBefore(before.get)
        if (exitedFilter.isDefined) cmd.withExitedFilter(exitedFilter.get)
        if (limit.isDefined) cmd.withLimit(limit.get)
        if (showAll.isDefined) cmd.withShowAll(showAll.get)
        if (showSize.isDefined) cmd.withShowSize(showSize.get)
        cmd.exec().asScala.toList
      }


    def listDockerRepositories(registryUrl: String): IO[OkHttpError, List[String]] =
      null
//      for {
//        url               <- UIO(s"$registryUrl/v2/_catalog")
//        repositoriesJson  <- okHttp.getAsJson(url, Map()).map(_ \ "repositories")
//        repositories      =  repositoriesJson.children.map(_.extract[String])
//      } yield repositories


    def listDockerTags(registryUrl: String,
                       image: String,
                       maximum: Option[Int] = None): IO[OkHttpError, List[String]] =
      null
//      for {
//        url           <- UIO(s"$registryUrl/v2/${image}/tags/list?n=${maximum.getOrElse(5)}")
//        repositories  <- okHttp.getAsJson(url, Map()).map(json => (json \ "tags").extract[List[String]])
//      } yield repositories


    def listImages(danglingFilter: Option[Boolean] = None,
                   imageNameFilter: Option[String] = None,
                   labelFilter: Map[String, String] = Map(),
                   showAll: Option[Boolean] = None): UIO[List[Image]] =
      client.map { c =>
        val cmd = c.listImagesCmd
          .withLabelFilter(labelFilter.asJava)
        if (danglingFilter.isDefined) cmd.withDanglingFilter(danglingFilter.get)
        if (imageNameFilter.isDefined) cmd.withImageNameFilter(imageNameFilter.get)
        if (showAll.isDefined) cmd.withShowAll(showAll.get)
        cmd.exec().asScala.toList
      }


    def listNetworks(filter: Option[(String, List[String])] = None,
                     idFilter: List[String] = List(),
                     nameFilter: List[String] = List()): IO[NotFoundException, List[Network]] =
      client.map { c =>
        val cmd = c.listNetworksCmd()
          .withIdFilter(idFilter: _*)
          .withNameFilter(nameFilter: _*)
        if (filter.isDefined) cmd.withFilter(filter.get._1, filter.get._2.asJava)
        cmd.exec().asScala.toList
      }


    def listServices(idFilter: List[String] = List(),
                     labelFilter: Map[String, String] = Map(),
                     nameFilter: List[String] = List()): IO[NotFoundException, List[DockerService]] =
      client.map(_.listServicesCmd()
        .withIdFilter(idFilter.asJava)
        .withLabelFilter(labelFilter.asJava)
        .withNameFilter(nameFilter.asJava)
        .exec().asScala.toList)


    def listSwarmNodes(idFilter: List[String] = List(),
                       membershipFilter: List[String] = List(),
                       nameFilter: List[String] = List(),
                       roleFilter: List[String] = List()): IO[NotFoundException, List[SwarmNode]] =
      client.map(_.listSwarmNodesCmd()
        .withIdFilter(idFilter.asJava)
        .withMembershipFilter(membershipFilter.asJava)
        .withNameFilter(nameFilter.asJava)
        .withRoleFilter(roleFilter.asJava)
        .exec().asScala.toList)


    def listTasks(idFilter: List[String] = List(),
                  labelFilter: Map[String, String] = Map(),
                  nameFilter: List[String] = List(),
                  nodeFilter: List[String] = List(),
                  serviceFilter: List[String] = List(),
                  stateFilter: List[TaskState] = List()): IO[NotFoundException, List[Task]] =
      client.map(_.listTasksCmd()
        .withIdFilter(idFilter: _*)
        .withLabelFilter(labelFilter.asJava)
        .withNameFilter(nameFilter: _*)
        .withNodeFilter(nodeFilter: _*)
        .withServiceFilter(serviceFilter: _*)
        .withStateFilter(stateFilter: _*)
        .exec().asScala.toList)


    def listVolumes(includeDangling: Boolean = true, filter: Option[(String, List[String])] = None): IO[NotFoundException, List[InspectVolumeResponse]] =
      client.map { c =>
        val cmd = c.listVolumesCmd.withDanglingFilter(includeDangling)
        if (filter.isDefined) cmd.withFilter(filter.get._1, filter.get._2.asJava)
        cmd.exec().getVolumes.asScala.toList
      }


    def loadImage(stream: InputStream): UIO[Unit] =
      client.map(_.loadImageCmd(stream).exec())


    def logContainer(id: ContainerId,
                     followStream: Option[Boolean] = None,
                     since: Option[Int] = None,
                     stderr: Option[Boolean] = None,
                     stdout: Option[Boolean] = None,
                     tail: Option[Int] = None,
                     timestamps: Option[Boolean] = None): UIO[Queue[Frame]] =
      for {
        c <- client
        q <- Queue.unbounded[Frame]
      } yield {
        val cmd = c.logContainerCmd(id)
        if (followStream.isDefined) cmd.withFollowStream(followStream.get)
        if (since.isDefined) cmd.withSince(since.get)
        if (stderr.isDefined) cmd.withStdErr(stderr.get)
        if (stdout.isDefined) cmd.withStdOut(stdout.get)
        if (tail.isDefined) cmd.withTail(tail.get)
        if (timestamps.isDefined) cmd.withTimestamps(timestamps.get)

        cmd.exec(
          new ResultCallback[Frame]() {
            override def onNext(item: Frame): Unit = q.offer(item)
            override def onError(throwable: Throwable): Unit = {}
            override def onStart(closeable: Closeable): Unit = {}
            override def onComplete(): Unit = {}
            override def close(): Unit = {}
          }
        )
        q
      }


    def logService(id: ServiceId,
                   details: Option[Boolean] = None,
                   follow: Option[Boolean] = None,
                   since: Option[Int] = None,
                   stdout: Option[Boolean] = None,
                   stderr: Option[Boolean] = None,
                   tail: Option[Int] = None,
                   timestamps: Option[Boolean] = None): UIO[Queue[Frame]] =
      for {
        c <- client
        q <- Queue.unbounded[Frame]
      } yield {
        val cmd = c.logServiceCmd(id)
        if (details.isDefined) cmd.withDetails(details.get)
        if (follow.isDefined) cmd.withFollow(follow.get)
        if (since.isDefined) cmd.withSince(since.get)
        if (stderr.isDefined) cmd.withStderr(stderr.get)
        if (stdout.isDefined) cmd.withStdout(stdout.get)
        if (tail.isDefined) cmd.withTail(tail.get)
        if (timestamps.isDefined) cmd.withTimestamps(timestamps.get)

        cmd.exec(
          new ResultCallback[Frame]() {
            override def onNext(item: Frame): Unit = q.offer(item)
            override def onError(throwable: Throwable): Unit = {}
            override def onStart(closeable: Closeable): Unit = {}
            override def onComplete(): Unit = {}
            override def close(): Unit = {}
          }
        )
        q
      }


    def logTask(id: ServiceId,
                details: Option[Boolean] = None,
                follow: Option[Boolean] = None,
                since: Option[Int] = None,
                stdout: Option[Boolean] = None,
                stderr: Option[Boolean] = None,
                tail: Option[Int] = None,
                timestamps: Option[Boolean] = None): UIO[Queue[Frame]] =
      for {
        c <- client
        q <- Queue.unbounded[Frame]
      } yield {
        val cmd = c.logTaskCmd(id)
        if (details.isDefined) cmd.withDetails(details.get)
        if (follow.isDefined) cmd.withFollow(follow.get)
        if (since.isDefined) cmd.withSince(since.get)
        if (stderr.isDefined) cmd.withStderr(stderr.get)
        if (stdout.isDefined) cmd.withStdout(stdout.get)
        if (tail.isDefined) cmd.withTail(tail.get)
        if (timestamps.isDefined) cmd.withTimestamps(timestamps.get)

        cmd.exec(
          new ResultCallback[Frame]() {
            override def onNext(item: Frame): Unit = q.offer(item)
            override def onError(throwable: Throwable): Unit = {}
            override def onStart(closeable: Closeable): Unit = {}
            override def onComplete(): Unit = {}
            override def close(): Unit = {}
          }
        )
        q
      }


    def pauseContainer(id: ContainerId): IO[NotFoundException, Unit] =
      client.map(_.pauseContainerCmd(id).exec())


    def ping: UIO[Unit] =
      client.map(_.pingCmd().exec())


    def prune(pruneType: PruneType,
              dangling: Option[Boolean] = None,
              labelFilter: List[String] = List(),
              untilFilter: Option[String] = None): IO[NotFoundException, Long] =
      client.map { c =>
        val cmd = c.pruneCmd(pruneType).withLabelFilter(labelFilter: _*)
        if (dangling.isDefined) cmd.withDangling(dangling.get)
        if (untilFilter.isDefined) cmd.withUntilFilter(untilFilter.get)
        cmd.exec().getSpaceReclaimed
      }


    def pullImage(repository: String,
                  authConfig: Option[AuthConfig] = None,
                  platform: Option[String] = None,
                  registry: Option[String] = None,
                  tag: Option[String] = None): IO[DockerException, Unit] =
      client.flatMap { c =>
        IO.effectAsync { cb =>
          val cmd = c.pullImageCmd(repository)
          if (authConfig.isDefined) cmd.withAuthConfig(authConfig.get)
          if (platform.isDefined) cmd.withPlatform(platform.get)
          if (registry.isDefined) cmd.withRegistry(registry.get)
          if (tag.isDefined) cmd.withTag(tag.get)

          cmd.exec(
            new ResultCallback[PullResponseItem]() {
              override def onNext(item: PullResponseItem): Unit = {}
              override def onError(throwable: Throwable): Unit = cb(IO.fail(throwable.asInstanceOf[DockerException]))
              override def onStart(closeable: Closeable): Unit = {}
              override def onComplete(): Unit = cb(IO.unit)
              override def close(): Unit = {}
            }
          )
        }
      }


    def pushImage(name: String,
                  authConfig: Option[AuthConfig] = None,
                  tag: Option[String] = None): IO[DockerException, Unit] =
      client.flatMap { c =>
        IO.effectAsync { cb =>
          val cmd = c.pushImageCmd(name)
          if (authConfig.isDefined) cmd.withAuthConfig(authConfig.get)
          if (tag.isDefined) cmd.withTag(tag.get)

          cmd.exec(
            new ResultCallback[PushResponseItem]() {
              override def onNext(item: PushResponseItem): Unit = {}
              override def onError(throwable: Throwable): Unit = cb(IO.fail(throwable.asInstanceOf[DockerException]))
              override def onStart(closeable: Closeable): Unit = {}
              override def onComplete(): Unit = cb(IO.unit)
              override def close(): Unit = {}
            }
          )
        }
      }


    def removeContainer(id: ContainerId, force: Boolean = false, removeVolumes: Boolean = false): IO[NotFoundException, Unit] =
      client.map(_.removeContainerCmd(id).withForce(force).withRemoveVolumes(removeVolumes).exec())


    def removeContainers(name: String, force: Boolean = false, removeVolumes: Boolean = false): IO[NotFoundException, Unit] =
      for {
        containers  <- listContainers(nameFilter = List(name))
        _           <- IO.foreach(containers)(c => removeContainer(c.getId, force, removeVolumes))
      } yield ()


    def removeImage(id: ImageId, force: Boolean = false, prune: Boolean = true): IO[NotFoundException, Unit] =
      client.map(_.removeImageCmd(id).withForce(force).withNoPrune(!prune).exec())


    def removeNetwork(id: NetworkId): IO[NotFoundException, Unit] =
      client.map(_.removeNetworkCmd(id).exec())


    def removeService(id: ServiceId): IO[NotFoundException, Unit] =
      client.map(_.removeServiceCmd(id).exec())


    def removeVolume(name: String): IO[NotFoundException, Unit] =
      client.map(_.removeVolumeCmd(name).exec())


    def renameContainer(id: ContainerId, name: String): UIO[Unit] =
      client.map(_.renameContainerCmd(id).withName(name).exec())


    def restartContainer(id: ContainerId, timeout: Option[Int] = None): IO[DockerException, Unit] =
      client.map { c =>
        val cmd = c.restartContainerCmd(id)
        if (timeout.isDefined) cmd.withTimeout(timeout.get)
        cmd.exec()
      }


    def saveImage(name: String, tag: Option[String] = None): IO[NotFoundException, InputStream] =
      client.map { c =>
        val cmd = c.saveImageCmd(name)
        if (tag.isDefined) cmd.withTag(tag.get)
        cmd.exec()
      }


    def searchImages(term: String): UIO[List[SearchItem]] =
      client.map(_.searchImagesCmd(term).exec().asScala.toList)


    def startContainer(id: ContainerId): IO[DockerException, Unit] =
      client.map(_.startContainerCmd(id).exec())


    def startLocalRegistry: IO[DockerException, Unit] =
      for {
        _ <- logger.info("Starting Docker Registry")
        _ <- IO.whenM(containerNotRunning("registry")) {
          for {
            _ <- logger.debug("Existing Docker Registry container not found. Starting a new one.")
            _ <- pullImage(registryImage)
            id <- createContainer("registry", registryImage, exposedPorts = Map(5000 -> 5000))
            _ <- startContainer(id)
          } yield ()
        }
      } yield ()


    def stats(id: ContainerId): IO[DockerException, Statistics] =
      client.flatMap { c =>
        IO.effectAsync { cb =>
          c.statsCmd(id).exec(
            new ResultCallback[Statistics]() {
              override def onNext(item: Statistics): Unit = cb(IO.succeed(item))
              override def onError(throwable: Throwable): Unit = cb(IO.fail(throwable.asInstanceOf[DockerException]))
              override def onStart(closeable: Closeable): Unit = {}
              override def onComplete(): Unit = {}
              override def close(): Unit = {}
            }
          )
        }
      }


    def stopContainer(id: ContainerId, timeout: Option[Int] = None): IO[DockerException, Unit] =
      client.map { c =>
        val cmd = c.stopContainerCmd(id)
        if (timeout.isDefined) cmd.withTimeout(timeout.get)
        cmd.exec()
      }


    def stopLocalRegistry: IO[DockerException, Unit] =
      for {
        _ <- logger.info("Stopping Zookeeper")
        containers <- listContainers(nameFilter = List("zookeeper"))
        _ <- IO.foreach_(containers.map(_.getId))(id => stopContainer(id))
      } yield ()

    def tagImage(id: ImageId, imageNameWithRepository: String, tag: String, force: Boolean = false): UIO[Unit] =
      client.map(_.tagImageCmd(id, imageNameWithRepository, tag).withForce(force).exec())


    def topContainer(id: ContainerId, psArgs: Option[String] = None): IO[NotFoundException, TopContainerResponse] =
      client.map { c =>
        val cmd = c.topContainerCmd(id)
        if (psArgs.isDefined) cmd.withPsArgs(psArgs.get)
        cmd.exec()
      }


    def unpauseContainer(id: ContainerId): IO[NotFoundException, Unit] =
      client.map(_.unpauseContainerCmd(id).exec())


    def updateContainer(id: ContainerId,
                        blkioWeight: Option[Int] = None,
                        cpuPeriod: Option[Int] = None,
                        cpuQuota: Option[Int] = None,
                        cpusetCpus: Option[String] = None,
                        cpusetMems: Option[String] = None,
                        cpuShares: Option[Int] = None,
                        kernelMemory: Option[Long] = None,
                        memory: Option[Long] = None,
                        memoryReservation: Option[Long] = None,
                        memorySwap: Option[Long] = None): IO[NotFoundException, UpdateContainerResponse] =
      client.map { c =>
        val cmd = c.updateContainerCmd(id)
        if (blkioWeight.isDefined) cmd.withBlkioWeight(blkioWeight.get)
        if (cpuPeriod.isDefined) cmd.withCpuPeriod(cpuPeriod.get)
        if (cpuQuota.isDefined) cmd.withCpuQuota(cpuQuota.get)
        if (cpusetCpus.isDefined) cmd.withCpusetCpus(cpusetCpus.get)
        if (cpusetMems.isDefined) cmd.withCpusetMems(cpusetMems.get)
        if (cpuShares.isDefined) cmd.withCpuShares(cpuShares.get)
        if (kernelMemory.isDefined) cmd.withKernelMemory(kernelMemory.get)
        if (memory.isDefined) cmd.withMemory(memory.get)
        if (memoryReservation.isDefined) cmd.withMemoryReservation(memoryReservation.get)
        if (memorySwap.isDefined) cmd.withMemorySwap(memorySwap.get)
        cmd.exec()
      }


    def updateService(id: ServiceId, spec: ServiceSpec): UIO[Unit] =
      client.map(_.updateServiceCmd(id, spec).exec())


    def updateSwarm(spec: SwarmSpec): UIO[Unit] =
      client.map(_.updateSwarmCmd(spec).exec())


    def updateSwarmNode(id: SwarmId, spec: SwarmNodeSpec, version: Option[Long] = None): IO[NotFoundException, Unit] =
      client.map { c =>
        val cmd = c.updateSwarmNodeCmd().withSwarmNodeId(id).withSwarmNodeSpec(spec)
        if (version.isDefined) cmd.withVersion(version.get)
        cmd.exec()
      }


    def version: UIO[Unit] =
      client.map(_.versionCmd().exec())


    def waitForContainer(id: ContainerId): IO[DockerException, Int] =
      client.flatMap { c =>
        IO.effectAsync { cb =>
          c.waitContainerCmd(id).exec(
            new ResultCallback[WaitResponse]() {
              override def onNext(item: WaitResponse): Unit = cb(IO.succeed(item.getStatusCode))
              override def onError(throwable: Throwable): Unit = cb(IO.fail(throwable.asInstanceOf[DockerException]))
              override def onStart(closeable: Closeable): Unit = {}
              override def onComplete(): Unit = {}
              override def close(): Unit = {}
            }
          )
        }
      }
  }
  }
}