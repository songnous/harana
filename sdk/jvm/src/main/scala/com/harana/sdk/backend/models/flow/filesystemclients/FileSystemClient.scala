package com.harana.sdk.backend.models.flow.filesystemclients

import org.joda.time.DateTime

import java.io._
import java.time.Instant

trait FileSystemClient {

  def fileExists(path: String): Boolean

  def saveObjectToFile[T <: Serializable](path: String, instance: T): Unit

  def copyLocalFile[T <: Serializable](localFilePath: String, remoteFilePath: String): Unit

  def saveInputStreamToFile(inputStream: InputStream, destinationPath: String): Unit

  def readFileAsObject[T <: Serializable](path: String): T

  def getFileInfo(path: String): Option[FileInfo]

  def delete(path: String): Unit

}

case class FileInfo(size: Long, modificationTime: Instant)

object FileSystemClient {

  def replaceLeadingTildeWithHomeDirectory(path: String) =
    if (path.startsWith("~/"))
      path.replaceFirst("~/", System.getProperty("user.home") + "/")
    else
      path
}
