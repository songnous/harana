package com.harana.modules.kind

import java.io.File

import com.harana.modules.kind.Kind.Service
import com.harana.modules.kind.models.Cluster
import com.harana.modules.core.logger.Logger
import zio.blocking.Blocking
import zio.process.Command
import zio.{Has, Task, UIO, ZLayer}

import scala.collection.mutable

object LiveKind {
  val layer = ZLayer.fromServices { (blocking: Blocking.Service,
                                     logger: Logger.Service) => new Service {

    def createCluster(name: String,
                      cluster: Option[Cluster] = None,
                      kubeConfig: Option[File] = None,
                      nodeImage: Option[String] = None,
                      retainNodesOnFailure: Boolean = false,
                      waitForControlPlane: Int = 0) =
      for {
        _ <- logger.info(s"Creating Kind cluster: $name")
        args <- UIO {
          val args = mutable.ListBuffer[String]("create", "cluster", "--name", name)
          if (cluster.isDefined) args += s"--config ${generateConfig(cluster.get)}"
          if (kubeConfig.isDefined) args += s"--kubeconfig ${kubeConfig.get.getAbsolutePath}"
          if (nodeImage.isDefined) args += s"--image ${nodeImage.get}"
          if (retainNodesOnFailure) args += s"--retain ${retainNodesOnFailure.toString}"
          if (waitForControlPlane > 0) args += s"--wait ${waitForControlPlane}s"
          args
        }
        cmd <- Command("kind", args.toSeq: _*).lines.provide(Has(blocking))
      } yield cmd.toList


    def deleteCluster(name: String,
                      kubeConfig: Option[File] = None): Task[Unit] =
      for {
        _ <- logger.info(s"Deleting Kind cluster: $name")
        args <- UIO {
          val args = mutable.ListBuffer[String]("delete", "cluster", "--name", name)
          if (kubeConfig.isDefined) args += s"--kubeconfig ${kubeConfig.get.getAbsolutePath}"
          args
        }
        _ <- Command("kind", args.toSeq: _*).lines.provide(Has(blocking))
      } yield ()


    def listClusters: Task[List[String]] =
      for {
        cmd <- Command("kind", "list", "nodes").lines.provide(Has(blocking))
      } yield cmd.toList


    def listNodes(name: String): Task[List[String]] =
      for {
        cmd <- Command("kind", List("--name", name): _*).lines.provide(Has(blocking))
      } yield cmd.toList


    def buildBaseImage(image: String): Task[Unit] =
      null


    def buildNodeImage(image: String): Task[Unit] =
      null


    def loadImage(image: String): Task[Unit] =
      null


    def exportLogs(path: Option[File] = None): Task[Unit] =
      null


    def exportKubeConfig(name: String,
                         path: Option[File] = None): Task[Unit] =
      null


    def printKubeConfig(name: String,
                        internalAddress: Boolean = false): Task[Unit] =
      null
  }}
}