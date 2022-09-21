package com.harana.sdk.backend.models.flow.actions

import java.sql.Timestamp
import org.apache.spark.sql.Row
import org.apache.spark.sql.types._
import org.scalatest.BeforeAndAfter
import com.harana.sdk.backend.models.flow.actions.inout._
import com.harana.sdk.backend.models.flow.{IntegratedTestSupport, TestFiles}
import com.harana.sdk.backend.models.flow.actionobjects.dataframe.DataFrame
import com.harana.sdk.backend.models.flow.actions.read.ReadDataFrame
import com.harana.sdk.backend.models.flow.actions.write.WriteDataFrame
import com.harana.sdk.shared.models.flow.actions.inout.{CsvParameters, InputFileFormatChoice, InputStorageTypeChoice, OutputFileFormatChoice, OutputStorageTypeChoice}

class WriteReadDataFrameWithDriverFilesIntegSpec extends IntegratedTestSupport with BeforeAndAfter with TestFiles {

  import IntegratedTestSupport._

  val schema: StructType =
    StructType(
      Seq(
        StructField("boolean", BooleanType),
        StructField("double", DoubleType),
        StructField("string", StringType)
      )
    )

  val rows = {
    val base = Seq(
      Row(true, 0.45, "3.14"),
      Row(false, null, "\"testing...\""),
      Row(false, 3.14159, "Hello, world!"),
      // in case of CSV, an empty string is the same as null - no way around it
      Row(null, null, "")
    )
    (1 to 10).flatMap(_ => base)
  }

  lazy val dataFrame = createDataFrame(rows, schema)

  "WriteDataFrame and ReadDataFrame" should {
    "write and read CSV file" in {
      val wdf =
        new WriteDataFrame()
          .setStorageType(
            new OutputStorageTypeChoice.File()
              .setOutputFile(absoluteTestsDirPath.fullPath + "/test_files")
              .setFileFormat(
                new OutputFileFormatChoice.Csv().setCsvColumnSeparator(CsvParameters.ColumnSeparatorChoice.Comma()).setNamesIncluded(true)
              )
          )
      wdf.executeUntyped(List(dataFrame))(executionContext)

      val rdf =
        new ReadDataFrame()
          .setStorageType(
            new InputStorageTypeChoice.File()
              .setSourceFile(absoluteTestsDirPath.fullPath + "/test_files")
              .setFileFormat(
                new InputFileFormatChoice.Csv()
                  .setCsvColumnSeparator(CsvParameters.ColumnSeparatorChoice.Comma())
                  .setNamesIncluded(true)
                  .setShouldConvertToBoolean(true)
              )
          )
      val loadedDataFrame = rdf.executeUntyped(List.empty)(executionContext).head
      assertDataFramesEqual(loadedDataFrame, dataFrame, checkRowOrder = false)
    }

    "write and read JSON file" in {
      val wdf =
        new WriteDataFrame()
          .setStorageType(
            new OutputStorageTypeChoice.File().setOutputFile(absoluteTestsDirPath.fullPath + "json").setFileFormat(new OutputFileFormatChoice.Json())
          )

      wdf.executeUntyped(List(dataFrame))(executionContext)

      val rdf =
        new ReadDataFrame()
          .setStorageType(
            new InputStorageTypeChoice.File().setSourceFile(absoluteTestsDirPath.fullPath + "json").setFileFormat(new InputFileFormatChoice.Json())
          )

      val loadedDataFrame = rdf.executeUntyped(List.empty)(executionContext).head
      assertDataFramesEqual(loadedDataFrame, dataFrame, checkRowOrder = false)
    }
  }
}