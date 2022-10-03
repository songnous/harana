package com.harana.sdk.backend.models.flow.actiontypes.readwritedataframe.filestorage

import com.harana.sdk.backend.models.flow.ExecutionContext
import com.harana.sdk.backend.models.flow.actiontypes.exceptions.HaranaIOError
import com.harana.sdk.backend.models.flow.actiontypes.readwritedataframe.FilePath
import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.{FileSystem, Path}

import java.io.{BufferedWriter, FileOutputStream, IOException, OutputStreamWriter}
import java.nio.file.{Files, Paths}
import java.util.UUID

private[filestorage] object FileDownloader {

  def downloadFile(url: String)(implicit context: ExecutionContext): FilePath = {
    if (context.tempPath.startsWith("hdfs://"))
      downloadFileToHdfs(url)
    else
      downloadFileToDriver(url)
  }

  private def downloadFileToHdfs(url: String)(implicit context: ExecutionContext) = {
    val content = scala.io.Source.fromURL(url).getLines()
    val hdfsPath = s"${context.tempPath}/${UUID.randomUUID()}"

    val configuration = new Configuration()
    val hdfs = FileSystem.get(configuration)
    val file = new Path(hdfsPath)
    val hdfsStream = hdfs.create(file)
    val writer = new BufferedWriter(new OutputStreamWriter(hdfsStream))
    try {
      content.foreach { s =>
        writer.write(s)
        writer.newLine()
      }
    } finally {
      safeClose(writer)
      hdfs.close()
    }

    FilePath(hdfsPath)
  }

  private def downloadFileToDriver(url: String)(implicit context: ExecutionContext) = {
    val outputDirPath = Paths.get(context.tempPath)
    // We're checking if the output is a directory following symlinks.
    // The default behaviour of createDirectories is NOT to follow symlinks
    if (!Files.isDirectory(outputDirPath))
      Files.createDirectories(outputDirPath)

    val outFilePath = Files.createTempFile(outputDirPath, "download", ".csv")
    // content is a stream. Do not invoke stuff like .toList() on it.
    val content = scala.io.Source.fromURL(url).getLines()
    val writer: BufferedWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outFilePath.toFile)))
    try {
      content.foreach { s =>
        writer.write(s)
        writer.newLine()
      }
    } finally
      safeClose(writer)
    FilePath(s"file:///$outFilePath")
  }

  private def safeClose(bufferedWriter: BufferedWriter) = {
    try {
      bufferedWriter.flush()
      bufferedWriter.close()
    } catch {
      case e: IOException => throw HaranaIOError(e).toException
    }
  }
}