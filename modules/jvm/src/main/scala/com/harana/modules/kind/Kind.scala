package com.harana.modules.kind

import java.io.File

import com.harana.modules.kind.models.Cluster
import zio.macros.accessible
import zio.{Has, Task}

@accessible
object Kind {
  type Kind = Has[Kind.Service]
  
  trait Service {

    def createCluster(name: String,
                      cluster: Option[Cluster] = None,
                      kubeConfig: Option[File] = None,
                      nodeImage: Option[String] = None,
                      retainNodesOnFailure: Boolean = false,
                      waitForControlPlane: Int = 0): Task[List[String]]

    def deleteCluster(name: String,
                      kubeConfig: Option[File] = None): Task[Unit]

    def listClusters: Task[List[String]]

    def listNodes(name: String): Task[List[String]]

    def buildBaseImage(image: String): Task[Unit]

    def buildNodeImage(image: String): Task[Unit]

    def loadImage(image: String): Task[Unit]

    def exportLogs(path: Option[File] = None): Task[Unit]

    def exportKubeConfig(name: String,
                         path: Option[File] = None): Task[Unit]

    def printKubeConfig(name: String,
                        internalAddress: Boolean = false): Task[Unit]
  }
}