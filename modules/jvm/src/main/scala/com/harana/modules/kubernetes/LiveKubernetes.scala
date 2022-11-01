package com.harana.modules.kubernetes

import akka.actor.ActorSystem
import akka.stream.Materializer
import akka.stream.scaladsl.{Sink, Source}
import akka.util.ByteString
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.harana.modules.core.config.Config
import com.harana.modules.core.logger.Logger
import com.harana.modules.core.micrometer.Micrometer
import play.api.libs.json.{Format, Json, Writes}
import skuber.api.Configuration
import skuber.api.client.{KubernetesClient, LoggingContext, RequestLoggingContext, WatchEvent}
import skuber.api.patch.Patch
import skuber.apiextensions.CustomResourceDefinition
import skuber.json.format.namespaceFormat
import skuber.{K8SException, k8sInit, _}
import zio._
import zio.interop.reactivestreams._
import zio.stream.{ZSink, ZStream}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{Future, Promise}

object LiveKubernetes { 

  private val yamlReader = new ObjectMapper(new YAMLFactory)
  private val jsonWriter = new ObjectMapper()
  private val actorSystem: Layer[Throwable, Has[ActorSystem]] = ZLayer.fromManaged(Managed.make(Task(ActorSystem("Test")))(sys => Task.fromFuture(_ => sys.terminate()).either))
  private val materializerLayer: Layer[Throwable, Has[Materializer]] = actorSystem >>> ZLayer.fromFunction(as => Materializer(as.get))

  val layer = ZLayer.fromServices { (config: Config.Service,
                                     logger: Logger.Service,
                                     micrometer: Micrometer.Service) => new Kubernetes.Service {

    def newClient: IO[K8SException, KubernetesClient] = {
      val cld = classOf[ActorSystem].getClassLoader
      implicit val system = ActorSystem("Kubernetes", classLoader = Some(cld))
      implicit val dispatcher = system.dispatcher
      Task(k8sInit(Configuration.parseKubeconfigFile().get)).refineToOrDie[K8SException]
    }


    def get[O <: ObjectResource](client: KubernetesClient, namespace: String, name: String)(implicit fmt: Format[O], rd: ResourceDefinition[O], lc: LoggingContext): IO[K8SException, Option[O]] =
      ZIO.fromFuture { _ =>  client.usingNamespace(namespace).getOption[O](name)(fmt, rd, lc) }.refineToOrDie[K8SException]
      
  
    def exists[O <: ObjectResource](client: KubernetesClient, namespace: String, name: String)(implicit fmt: Format[O], rd: ResourceDefinition[O], lc: LoggingContext): IO[K8SException, Boolean] =
      ZIO.fromFuture { _ =>  client.usingNamespace(namespace).getOption(name)(fmt, rd, lc).map(_.isDefined) }.refineToOrDie[K8SException]


    def save(client: KubernetesClient, namespace: String, crd: CustomResourceDefinition): IO[K8SException, CustomResourceDefinition] =
      ZIO.fromFuture { _ =>  
        client.usingNamespace(namespace).create(crd).recoverWith {
          case alreadyExists: K8SException if alreadyExists.status.code.contains(409) =>
            client.get[CustomResourceDefinition](crd.name).flatMap { existing =>
              val currentVersion = existing.metadata.resourceVersion
              val newMeta = crd.metadata.copy(resourceVersion = currentVersion)
              val updatedObj = crd.copy(metadata = newMeta)
              client.update(updatedObj)
            }
        }      
          
      }.refineToOrDie[K8SException]
      

    def createNamespace(client: KubernetesClient, namespace: String)(implicit lc: LoggingContext): IO[K8SException, Namespace] =
      ZIO.fromFuture { _ =>  client.create[Namespace](Namespace(namespace)) }.refineToOrDie[K8SException]


    def create[O <: ObjectResource](client: KubernetesClient, namespace: String, obj: O)(implicit fmt: Format[O], rd: ResourceDefinition[O], lc: LoggingContext): IO[K8SException, O] =
      ZIO.fromFuture { _ =>  client.usingNamespace(namespace).create[O](obj)(fmt, rd, lc) }.refineToOrDie[K8SException]


    def update[O <: ObjectResource](client: KubernetesClient, namespace: String, obj: O)(implicit fmt: Format[O], rd: ResourceDefinition[O], lc: LoggingContext): IO[K8SException, O] =
      ZIO.fromFuture { _ =>  client.usingNamespace(namespace).update[O](obj)(fmt, rd, lc) }.refineToOrDie[K8SException]
      

    def delete[O <: ObjectResource](client: KubernetesClient, namespace: String, name: String, gracePeriodSeconds: Int = -1)(implicit rd: ResourceDefinition[O], lc: LoggingContext): IO[K8SException, Unit] =
      ZIO.fromFuture { _ =>  client.usingNamespace(namespace).delete[O](name, gracePeriodSeconds)(rd, lc) }.refineToOrDie[K8SException]
      

    def deleteWithOptions[O <: ObjectResource](client: KubernetesClient, namespace: String, name: String, options: DeleteOptions)(implicit rd: ResourceDefinition[O], lc: LoggingContext): IO[K8SException, Unit] =
      ZIO.fromFuture { _ =>  client.usingNamespace(namespace).deleteWithOptions[O](name, options)(rd, lc) }.refineToOrDie[K8SException]
      

    def deleteAll[O <: ObjectResource](client: KubernetesClient, namespace: String)(implicit fmt: Format[ListResource[O]], rd: ResourceDefinition[ListResource[O]], lc: LoggingContext): IO[K8SException, ListResource[O]] =
      ZIO.fromFuture { _ =>  client.usingNamespace(namespace).deleteAll[ListResource[O]]()(fmt, rd, lc) }.refineToOrDie[K8SException]
      

    def deleteAllSelected[O <: ObjectResource](client: KubernetesClient, namespace: String, labelSelector: LabelSelector)(implicit fmt: Format[ListResource[O]], rd: ResourceDefinition[ListResource[O]], lc: LoggingContext): IO[K8SException, ListResource[O]] =
      ZIO.fromFuture { _ =>  client.usingNamespace(namespace).deleteAllSelected[ListResource[O]](labelSelector)(fmt, rd, lc) }.refineToOrDie[K8SException]
      

    def getNamespaceNames(client: KubernetesClient)(implicit lc: LoggingContext): IO[K8SException, List[String]] =
      ZIO.fromFuture { _ =>  client.getNamespaceNames(lc) }.refineToOrDie[K8SException]
      

    def listByNamespace[O <: ObjectResource](client: KubernetesClient)(implicit fmt: Format[ListResource[O]], rd: ResourceDefinition[ListResource[O]], lc: LoggingContext): IO[K8SException, Map[String, ListResource[O]]] =
      ZIO.fromFuture { _ =>  client.listByNamespace[ListResource[O]]()(fmt, rd, lc) }.refineToOrDie[K8SException]
      

    def list[O <: ObjectResource](client: KubernetesClient, namespace: String)(implicit fmt: Format[ListResource[O]], rd: ResourceDefinition[ListResource[O]], lc: LoggingContext): IO[K8SException, ListResource[O]] =
      ZIO.fromFuture { _ =>  client.usingNamespace(namespace).list[ListResource[O]]()(fmt, rd, lc) }.refineToOrDie[K8SException]
      

    def listSelected[O <: ObjectResource](client: KubernetesClient, namespace: String, labelSelector: LabelSelector)(implicit fmt: Format[ListResource[O]], rd: ResourceDefinition[ListResource[O]], lc: LoggingContext): IO[K8SException, ListResource[O]] =
      ZIO.fromFuture { _ =>  client.usingNamespace(namespace).listSelected[ListResource[O]](labelSelector)(fmt, rd, lc) }.refineToOrDie[K8SException]
      
 
    def listWithOptions[O <: ObjectResource](client: KubernetesClient, namespace: String, options: ListOptions)(implicit fmt: Format[ListResource[O]], rd: ResourceDefinition[ListResource[O]], lc: LoggingContext): IO[K8SException, ListResource[O]] =
      ZIO.fromFuture { _ =>  client.usingNamespace(namespace).listWithOptions[ListResource[O]](options)(fmt, rd, lc) }.refineToOrDie[K8SException]
      

    def updateStatus[O <: ObjectResource](client: KubernetesClient, namespace: String, obj: O)(implicit fmt: Format[O], rd: ResourceDefinition[O], statusEv: HasStatusSubresource[O], lc: LoggingContext): IO[K8SException, O] =
      ZIO.fromFuture { _ =>  client.usingNamespace(namespace).updateStatus[O](obj)(fmt, rd, statusEv, lc) }.refineToOrDie[K8SException]
      

    def getStatus[O <: ObjectResource](client: KubernetesClient, namespace: String, name: String)(implicit fmt: Format[O], rd: ResourceDefinition[O], statusEv: HasStatusSubresource[O], lc: LoggingContext): IO[K8SException, O] =
      ZIO.fromFuture { _ =>  client.usingNamespace(namespace).getStatus[O](name)(fmt, rd, statusEv, lc) }.refineToOrDie[K8SException]
      

    def watch[O <: ObjectResource](client: KubernetesClient, namespace: String, obj: O)(implicit fmt: Format[O], rd: ResourceDefinition[O], lc: LoggingContext): IO[K8SException, Source[WatchEvent[O], _]] =
      ZIO.fromFuture { _ =>  client.usingNamespace(namespace).watch[O](obj)(fmt, rd, lc) }.refineToOrDie[K8SException]
      

    def watch[O <: ObjectResource](client: KubernetesClient, namespace: String, name: String, sinceResourceVersion: Option[String] = None, bufSize: Int = 10000)(implicit fmt: Format[O], rd: ResourceDefinition[O], lc: LoggingContext): IO[K8SException, Source[WatchEvent[O], _]] =
      ZIO.fromFuture { _ =>  client.usingNamespace(namespace).watch[O](name, sinceResourceVersion, bufSize)(fmt, rd, lc) }.refineToOrDie[K8SException]
      

    def watchAll[O <: ObjectResource](client: KubernetesClient, namespace: String, sinceResourceVersion: Option[String] = None, bufSize: Int = 10000)(implicit fmt: Format[O], rd: ResourceDefinition[O], lc: LoggingContext): IO[K8SException, Source[WatchEvent[O], _]] =
      ZIO.fromFuture { _ =>  client.usingNamespace(namespace).watchAll[O](sinceResourceVersion, bufSize)(fmt, rd, lc) }.refineToOrDie[K8SException]
      

    def watchContinuously[O <: ObjectResource](client: KubernetesClient, namespace: String, obj: O)(implicit fmt: Format[O], rd: ResourceDefinition[O], lc: LoggingContext): IO[K8SException, Source[WatchEvent[O], _]] =
      IO(client.usingNamespace(namespace).watchContinuously[O](obj)(fmt, rd, lc)).refineToOrDie[K8SException]


    def watchContinuously[O <: ObjectResource](client: KubernetesClient, namespace: String, name: String, sinceResourceVersion: Option[String] = None, bufSize: Int = 10000)(implicit fmt: Format[O], rd: ResourceDefinition[O], lc: LoggingContext): IO[K8SException, Source[WatchEvent[O], _]] =
      IO(client.usingNamespace(namespace).watchContinuously[O](name, sinceResourceVersion, bufSize)(fmt, rd, lc)).refineToOrDie[K8SException]


    def watchAllContinuously[O <: ObjectResource](client: KubernetesClient, namespace: String, sinceResourceVersion: Option[String] = None, bufSize: Int = 10000)(implicit fmt: Format[O], rd: ResourceDefinition[O], lc: LoggingContext): IO[K8SException, Source[WatchEvent[O], _]] =
      IO(client.usingNamespace(namespace).watchAllContinuously[O](sinceResourceVersion, bufSize)(fmt, rd, lc)).refineToOrDie[K8SException]


    def watchWithOptions[O <: ObjectResource](client: KubernetesClient, namespace: String, options: ListOptions, bufSize: Int = 10000)(implicit fmt: Format[O], rd: ResourceDefinition[O], lc: LoggingContext): IO[K8SException, Source[WatchEvent[O], _]] =
      IO(client.usingNamespace(namespace).watchWithOptions[O](options, bufSize)(fmt, rd, lc)).refineToOrDie[K8SException]


    def getScale[O <: ObjectResource](client: KubernetesClient, namespace: String, objName: String)(implicit rd: ResourceDefinition[O], sc: Scale.SubresourceSpec[O], lc: LoggingContext) : IO[K8SException, Scale] =
      ZIO.fromFuture { _ =>  client.usingNamespace(namespace).getScale[O](objName)(rd, sc, lc) }.refineToOrDie[K8SException]
      

    def updateScale[O <: ObjectResource](client: KubernetesClient, namespace: String, objName: String, scale: Scale)(implicit rd: ResourceDefinition[O], sc: Scale.SubresourceSpec[O], lc: LoggingContext): IO[K8SException, Scale] =
      ZIO.fromFuture { _ =>  client.usingNamespace(namespace).updateScale[O](objName, scale)(rd, sc, lc) }.refineToOrDie[K8SException]
      

    def patch[P <: Patch, O <: ObjectResource](client: KubernetesClient, namespace: String, name: String, patchData: P)(implicit patchfmt: Writes[P], fmt: Format[O], rd: ResourceDefinition[O], lc: LoggingContext = RequestLoggingContext()): IO[K8SException, O] =
      ZIO.fromFuture { _ =>  client.patch[P, O](name, patchData, Some(namespace))(patchfmt, fmt, rd, lc) }.refineToOrDie[K8SException]


    def getPodLogSource(client: KubernetesClient, namespace: String, name: String, queryParams: Pod.LogQueryParams)(implicit lc: LoggingContext): IO[K8SException, Source[ByteString, _]] =
      ZIO.fromFuture { _ =>  client.getPodLogSource(name, queryParams, Some(namespace))(lc) }.refineToOrDie[K8SException]


    def exec(client: KubernetesClient,
             namespace: String,
             podName: String,
             command: Seq[String],
             maybeContainerName: Option[String] = None,
             maybeStdin: Option[ZStream[Any, Nothing, String]] = None,
             maybeStdout: Option[String => UIO[Unit]] = None,
             maybeStderr: Option[String => UIO[Unit]] = None,
             tty: Boolean = false,
             maybeClose: Option[Promise[Unit]] = None)(implicit lc: LoggingContext): IO[K8SException, Unit] = {
      for {
        source    <- if (maybeStdin.isDefined)
                      for {
                        publisher <- maybeStdin.get.toPublisher
                        source    =  Some(Source.fromPublisher(publisher))
                      } yield source
                     else ZIO.none

        sinkOut   =  if (maybeStdout.isDefined) Some(Sink.foreach[String](s => maybeStdout.get(s).toFuture)) else None
        sinkErr   =  if (maybeStderr.isDefined) Some(Sink.foreach[String](s => maybeStderr.get(s).toFuture)) else None

        _         <- ZIO.fromFuture { _ =>
                       client.usingNamespace(namespace).exec(podName, command, maybeContainerName, source, sinkOut, sinkErr, tty, maybeClose)(lc)
                     }.refineToOrDie[K8SException]
      } yield ()
    }


    def getServerAPIVersions(client: KubernetesClient)(implicit lc: LoggingContext): IO[K8SException, List[String]] =
      ZIO.fromFuture { _ =>  client.getServerAPIVersions(lc) }.refineToOrDie[K8SException]
      

    def resourceFromFile[A <: ObjectResource](fileName: String)(implicit fmt: Format[A]): Task[A] =
      for {
        yaml        <- Task(scala.io.Source.fromResource(fileName).mkString)
        obj         <- Task(yamlReader.readValue(yaml, classOf[Object]))
                      .onError(ex => logger.info(s"Failed to parse YAML for: $fileName with message: ${ex.prettyPrint}"))
        json        <- Task(jsonWriter.writeValueAsString(obj))
        resource    <- Task(Json.parse(json).as[A])
                      .onError(ex => logger.info(s"Failed to convert YAML to object for: $fileName with message: ${ex.prettyPrint}"))
      } yield resource


    def close(client: KubernetesClient): IO[K8SException, Unit] =
      IO(client.close).refineToOrDie[K8SException]
  }}
}