package com.harana.workflowexecutor

import java.io._
import java.util.zip.ZipInputStream
import scala.reflect.io.Path
import com.harana.sdk.backend.models.designer.flow.utils.Logging

import java.nio.file.Files
import scala.annotation.tailrec

object Unzip extends Logging {

  def unzipToTmp(inputFile: String, filter: (String) => Boolean): String = {
    val zis = new ZipInputStream(new FileInputStream(inputFile))
    val tempDir = Files.createTempDirectory("harana").toFile
    logger.info(s"Created temporary directory for $inputFile: ${tempDir.getAbsolutePath}")

    var entry = zis.getNextEntry
    while (entry != null) {
      if (filter(entry.getName)) {
        val path = Path(entry.getName)
        val entryFilename = path.name
        val entryDirName  = path.parent

        logger.debug("Entry found in jar file: directory: $entryDirName filename: $entryFilename isDirectory: ${entry.isDirectory}")

        val destinationPath = Path(tempDir) / entryDirName
        new File(destinationPath.toURI).mkdirs()
        if (!entry.isDirectory) {
          val target = new File((destinationPath / entryFilename).toURI)
          val fos    = new BufferedOutputStream(new FileOutputStream(target, true))
          transferImpl(zis, fos)
        }
      }
      entry = zis.getNextEntry
    }
    zis.close()
    tempDir.toString
  }

  def unzipAll(inputFile: String) = unzipToTmp(inputFile, _ => true)

  private def transferImpl(in: InputStream, out: OutputStream): Unit = {
    try {
      val buffer = new Array[Byte](4096)
      @tailrec
      def read(): Unit = {
        val byteCount = in.read(buffer)
        if (byteCount >= 0) {
          out.write(buffer, 0, byteCount)
          read()
        }
      }
      read()
      out.close()
    } finally {
      in.close()
    }
  }
}
