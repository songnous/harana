package com.harana.modules.zookeeper

import com.harana.modules.docker.Docker
import com.harana.modules.core.logger.Logger
import com.harana.modules.zookeeper.Zookeeper.Service
import zio.{Task, UIO, ZLayer}

object LiveZookeeper {
  val layer = ZLayer.fromServices { (docker: Docker.Service,
                                     logger: Logger.Service) => new Service {

    private val image = "zookeeper:3.6"

    def localStart: Task[Unit] =
      for {
        _ <- logger.info("Starting Zookeeper")
        running <- docker.containerRunning("zookeeper")
        _ <- logger.debug("Existing Zookeeper container not found. Starting a new one.").when(!running)
        _ <- start.when(!running)
      } yield ()

    def localStop: Task[Unit] =
      for {
        _ <- logger.info("Stopping Zookeeper")
        containers <- docker.listContainers(nameFilter = List("zookeeper"))
        _ <- Task.foreach(containers.map(_.getId))(id => docker.stopContainer(id))
      } yield ()

    private def start =
      for {
        _ <- docker.pullImage(image)
        id <- docker.createContainer("zookeeper", image, exposedPorts = Map(2181 -> 2181))
        _ <- docker.startContainer(id)
      } yield ()
  }}
}