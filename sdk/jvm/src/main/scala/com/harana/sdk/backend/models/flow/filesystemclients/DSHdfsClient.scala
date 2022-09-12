package com.harana.sdk.backend.models.flow.filesystemclients

import com.harana.sdk.backend.models.designer.flow.utils.Serialization
import com.harana.sdk.backend.models.flow.utils.Serialization
import org.apache.hadoop.hdfs.{DFSClient, DFSInputStream}

import java.io._
import java.time.Instant

/** Wrapper class for DFSClient. Introduces higher level operations on hdfs. */
case class DSHdfsClient(hdfsClient: DFSClient) extends Serialization with FileSystemClient {

  /** Checks if file located by the given path exists */
  def fileExists(path: String): Boolean = hdfsClient.exists(path)

  /** Serializes given object using default java serialization and saves it to a file under the given path */
  def saveObjectToFile[T <: Serializable](path: String, instance: T) = {
    val inputStream = new BufferedInputStream(new ByteArrayInputStream(serialize(instance)))
    try
      saveInputStreamToFile(inputStream, path)
    finally
      inputStream.close()
  }

  /** Copies file from the local files system to the hdfs. */
  def copyLocalFile[T <: Serializable](localFilePath: String, remoteFilePath: String) = {
    val localFromFile = new File(localFilePath)
    if (localFromFile.isDirectory) {
      hdfsClient.mkdirs(remoteFilePath, null, true)
      localFromFile.listFiles.foreach(f => copyLocalFile(f.getPath, remoteFilePath + "/" + f.getName))
    } else {
      val inputStream = new BufferedInputStream(new FileInputStream(localFilePath))
      try
        saveInputStreamToFile(inputStream, remoteFilePath)
      finally
        inputStream.close()
    }
  }

  /** Saves content of the given input stream to the file on hdfs under the given path. */
  def saveInputStreamToFile(inputStream: InputStream, destinationPath: String) = {
    val fos = new BufferedOutputStream(hdfsClient.create(destinationPath, false))
    try
      org.apache.commons.io.IOUtils.copy(inputStream, fos)
    finally
      fos.close()
  }

  /** Reads content of the file under the given path and uses default java serialization to deserialize it to the
    * instance of a class with the given type.
    */
  def readFileAsObject[T <: Serializable](path: String): T = {
    val inputStream: DFSInputStream = hdfsClient.open(path)
    deserialize(org.apache.commons.io.IOUtils.toByteArray(inputStream))
  }

  /** Returns basic info about a file. */
  def getFileInfo(path: String): Option[FileInfo] = Option(hdfsClient.getFileInfo(path))
    .map(s => FileInfo(s.getLen, Instant.ofEpochMilli(s.getModificationTime)))

  /** Deletes file or dir under given path */
  def delete(path: String) = hdfsClient.delete(path, true)

}
