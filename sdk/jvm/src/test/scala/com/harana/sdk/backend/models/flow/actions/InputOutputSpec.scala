package com.harana.sdk.backend.models.flow.actions

import com.harana.sdk.backend.models.flow.utils.Logging
import org.scalatest._
import org.scalatest.freespec.AnyFreeSpec

@DoNotDiscover
class InputOutputSpec extends AnyFreeSpec with BeforeAndAfter with BeforeAndAfterAll with Logging {
//class InputOutputSpec extends AnyFreeSpec with BeforeAndAfter with BeforeAndAfterAll with TestFiles with Logging {

  /*

  import DataFrameMatchers._

  implicit lazy val ctx = StandaloneSparkClusterForTests.executionContext

  private val someFormatsSupportedByDriver = Seq(
    new InputFileFormatChoice.Csv()
      .setCsvColumnSeparator(ColumnSeparatorChoice.Comma())
      .setNamesIncluded(true)
      .setShouldConvertToBoolean(true),
    new InputFileFormatChoice.Json.Null
  )

  private val someFormatsSupportedByCluster = someFormatsSupportedByDriver :+ new InputFileFormatChoice.Parquet()

  assume(
    {
      val clusterClasses = someFormatsSupportedByCluster.map(_.getClass).toSet
      val allClasses = InputFileFormatChoice.choiceOrder.toSet
      clusterClasses == allClasses
    },
    s"""All formats are supported on cluster - if this assumption no longer
       |holds you probably need to either fix production
       |code and/or add test files or change this test.""".stripMargin
  )

  "Files with" - {
    val schemes = List(FileScheme.File, FileScheme.HTTPS)
    for (fileScheme <- schemes) {
      s"'${fileScheme.pathPrefix}' scheme path of" - {
        for (driverFileFormat <- someFormatsSupportedByDriver) {
          s"$driverFileFormat format work on driver" - {
            for (clusterFileFormat <- someFormatsSupportedByCluster) {
              s"with $clusterFileFormat format works on cluster" in {

                info("Reading file on driver")
                val path = testFile(driverFileFormat, fileScheme)
                val dataframe = read(path, driverFileFormat)

                info("Saving dataframe to HDFS")
                val someHdfsTmpPath = StandaloneSparkClusterForTests.generateSomeHdfsTmpPath()
                write(someHdfsTmpPath, OutputFromInputFileFormat(clusterFileFormat))(dataframe)

                info("Reading dataframe from HDFS back")
                val dataframeReadBackFromHdfs = read(someHdfsTmpPath, clusterFileFormat)
                assertDataFramesEqual(dataframeReadBackFromHdfs, dataframe, checkRowOrder = false)

                info("Writing dataframe back on driver")
                val someDriverTmpPath = generateSomeDriverTmpPath()
                write(
                  someDriverTmpPath,
                  OutputFromInputFileFormat(driverFileFormat)
                )(dataframeReadBackFromHdfs)

                info("Dataframe contains same data after all those actions")
                val finalDataframe = read(someDriverTmpPath, driverFileFormat)
                assertDataFramesEqual(finalDataframe, dataframe, checkRowOrder = false)
              }
            }
          }
        }
      }
    }
  }

  private def generateSomeDriverTmpPath() = absoluteTestsDirPath.fullPath + "tmp-" + UUID.randomUUID() + ".data"

  private def read(path: String, fileFormat: InputFileFormatChoice) = {
    val readDF = new ReadDataFrame()
      .setStorageType(
        new InputStorageTypeChoice.File()
          .setSourceFile(path)
          .setFileFormat(fileFormat)
      )
    readDF.executeUntyped(List.empty[ActionObject])(ctx).head.asInstanceOf[DataFrame]
  }

  private def write(path: String, fileFormat: OutputFileFormatChoice)(dataframe: DataFrame) = {
    val write = new WriteDataFrame()
      .setStorageType(
        new OutputStorageTypeChoice.File()
          .setOutputFile(path)
          .setFileFormat(fileFormat)
      )
    write.executeUntyped(List(dataframe))(ctx)
  }

  */
}
