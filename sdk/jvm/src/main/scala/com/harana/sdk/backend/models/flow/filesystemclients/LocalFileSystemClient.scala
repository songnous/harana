package com.harana.sdk.backend.models.flow.filesystemclients

import com.harana.sdk.backend.models.designer.flow.utils.{ManagedResource, Serialization}
import com.harana.sdk.backend.models.flow.utils.Serialization
import org.apache.hadoop.fs.FileUtil

import java.io._
import java.nio.file.{Files, Paths}
import java.time.Instant

case class LocalFileSystemClient() extends FileSystemClient with Serialization {

  def fileExists(path: String): Boolean = Files.exists(Paths.get(path))

  def copyLocalFile[T <: Serializable](localFilePath: String, remoteFilePath: String) = {
    def copyFile(f: File, dest: String) =
      ManagedResource(new FileInputStream(f))(fis => saveInputStreamToFile(fis, dest))
    val input                                 = new File(localFilePath)
    if (input.isDirectory)
      input.listFiles().foreach(f => copyFile(f, remoteFilePath + "/" + f.getName))
    else
      copyFile(input, remoteFilePath)

  }

  def saveObjectToFile[T <: Serializable](path: String, instance: T) = {
    val inputStream = new BufferedInputStream(new ByteArrayInputStream(serialize(instance)))
    ManagedResource(inputStream)(inputStream => saveInputStreamToFile(inputStream, path))
  }

  def saveInputStreamToFile(inputStream: InputStream, destinationPath: String) =
    ManagedResource(new BufferedOutputStream(new FileOutputStream(destinationPath))) { fos =>
      org.apache.commons.io.IOUtils.copy(inputStream, fos)
    }

  def readFileAsObject[T <: Serializable](path: String): T =
    ManagedResource(new FileInputStream(path)) { inputStream =>
      deserialize(org.apache.commons.io.IOUtils.toByteArray(inputStream))
    }

  def getFileInfo(path: String): Option[FileInfo] = {
    val file = new File(path)
    if (file.exists())
      Some(FileInfo(file.length(), Instant.ofEpochMilli(file.lastModified())))
    else
      None
  }

  def delete(path: String) = FileUtil.fullyDelete(new File(path))

}
