package com.harana.modules.alluxiofs

import alluxio.client.file.URIStatus
import alluxio.exception.AlluxioException
import alluxio.grpc.SetAclAction
import alluxio.security.authorization.AclEntry
import com.harana.shared.models.HaranaFile
import zio.macros.accessible
import zio.{Has, IO, Task}

@accessible
object AlluxioFs {
  type AlluxioFs = Has[AlluxioFs.Service]

  trait Service {
    def createDirectory(path: String,
                        createParent: Boolean,
                        username: Option[String] = None): Task[Unit]

    def createFile(path: String,
                   data: Array[Byte],
                   username: Option[String] = None,
                   blockSize: Option[Int] = None): Task[Unit]

    def delete(path: String,
               recursive: Boolean,
               username: Option[String] = None): Task[Unit]

    def exists(path: String,
               username: Option[String] = None): Task[Boolean]

    def free(path: String,
             username: Option[String] = None): Task[Unit]

    def info(path: String,
             username: Option[String] = None): Task[HaranaFile]

    def isDirectory(path: String,
                    username: Option[String] = None): Task[Boolean]

    def isFile(path: String,
               username: Option[String] = None): Task[Boolean]

    def list(path: String,
             username: Option[String] = None): Task[List[HaranaFile]]

    def loadFile(path: String,
                 username: Option[String] = None): Task[Array[Byte]]

    def mount(path: String,
              ufsPath: String,
              username: Option[String] = None): Task[Unit]

    def parent(path: String,
               username: Option[String] = None): Task[Option[String]]

    def persist(path: String,
                username: Option[String] = None): Task[Unit]

    def rename(source: String,
               destination: String,
               username: Option[String] = None): Task[Unit]

    def search(path: String, query: String): Task[List[HaranaFile]]

    def setAcl(path: String,
               action: SetAclAction,
               entries: List[AclEntry],
               username: Option[String] = None): Task[Unit]

    def unmount(path: String,
                username: Option[String] = None): Task[Unit]
  }
}