package com.harana.sdk.backend.models.flow

import org.scalatest.BeforeAndAfter
import org.scalatest.BeforeAndAfterAll
import com.harana.sdk.backend.models.flow.actions.readwritedataframe.{FilePath, FileScheme}
import com.harana.sdk.backend.models.flow.filesystemclients.LocalFileSystemClient
import com.harana.sdk.shared.models.flow.actions.inout.InputFileFormatChoice

trait TestFiles { self: BeforeAndAfter with BeforeAndAfterAll =>

  private def fileSystemClient = LocalFileSystemClient()
  private val testsDir = "target/tests"

  before {
    fileSystemClient.delete(testsDir)
    new java.io.File(testsDir + "/id").getParentFile.mkdirs()
    fileSystemClient.copyLocalFile(getClass.getResource("/test_files/").getPath, testsDir)
  }

  after {
    fileSystemClient.delete(testsDir)
  }

  def testFile(fileFormat: InputFileFormatChoice, fileScheme: FileScheme) = {
    val format = fileFormat.getClass.getSimpleName.toLowerCase()
    val fileName = s"some_$format.$format"
    val path = fileScheme match {
      case FileScheme.HTTPS => "https://s3.amazonaws.com/workflowexecutor/test_data/"
      case FileScheme.File  => absoluteTestsDirPath.fullPath
      case other            => throw new IllegalStateException(s"$other not supported")
    }
    val fullPath = path + fileName
    fullPath
  }

  def someCsvFile = FilePath(FileScheme.File, testsDir + "/some_csv.csv")

  def absoluteTestsDirPath: FilePath = FilePath(FileScheme.File, rawAbsoluteTestsDirPath)

  private def rawAbsoluteTestsDirPath = new java.io.File(testsDir).getAbsoluteFile.toString + "/"

}
