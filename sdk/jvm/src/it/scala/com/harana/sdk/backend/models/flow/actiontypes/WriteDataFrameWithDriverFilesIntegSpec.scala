package com.harana.sdk.backend.models.flow.actiontypes

import com.harana.sdk.backend.models.flow.actiontypes.exceptions.{HaranaIOError, UnsupportedColumnTypeError}
import com.harana.sdk.backend.models.flow.actiontypes.readwritedataframe.FileScheme.File
import com.harana.sdk.backend.models.flow.actiontypes.readwritedataframe.UnknownFileSchemaForPath
import com.harana.sdk.backend.models.flow.actiontypes.readwritedataframe.filestorage.ParquetNotSupported
import com.harana.sdk.backend.models.flow.actiontypes.write.WriteDataFrame
import com.harana.sdk.backend.models.flow.{IntegratedTestSupport, Knowledge, TestFiles}
import com.harana.sdk.shared.models.flow.actiontypes.inout.{CsvParameters, OutputFileFormatChoice, OutputStorageTypeChoice}
import com.harana.spark.CSV
import org.apache.spark.sql._
import org.apache.spark.sql.types._
import org.scalatest.BeforeAndAfter

import java.sql.Timestamp
import java.time.Instant
import scala.io.Source

class WriteDataFrameWithDriverFilesIntegSpec extends IntegratedTestSupport with BeforeAndAfter with TestFiles {

  val dateTime = Instant.now

  val timestamp = new Timestamp(dateTime.toEpochMilli)

  val schema: StructType = StructType(
    Seq(
      StructField("boolean", BooleanType),
      StructField("double", DoubleType),
      StructField("string", StringType),
      StructField("timestamp", TimestampType)
    )
  )

  val rows = Seq(
    Row(true, 0.45, "3.14", timestamp),
    Row(false, null, "\"testing...\"", null),
    Row(false, 3.14159, "Hello, world!", timestamp),
    Row(null, null, null, null)
  )

  val quoteChar = "\""

  def quote(value: String, sep: String) = {
    def escapeQuotes(x: Any) = s"$x".replace(quoteChar, CSV.EscapeQuoteChar + quoteChar)

    def optionallyQuote(x: String) =
      if (x.contains(sep) || x.contains(quoteChar))
        s"$quoteChar$x$quoteChar"
      else
        x

    (escapeQuotes _).andThen(optionallyQuote)(value)
  }

  def rowsAsCsv(sep: String): Seq[String] = {
    val rows = Seq(
      Seq("1", "0.45", "3.14", DateTimeConverter.toString(dateTime)),
      Seq("0", "", "\"testing...\"", ""),
      Seq("0", "3.14159", "Hello, world!", DateTimeConverter.toString(dateTime)),
      Seq("", "", "", "")
    ).map(row => row.map(quote(_, sep)).mkString(sep))
    rows.map(CSV.additionalEscapings(sep))
  }

  lazy val dataframe = createDataFrame(rows, schema)
  val arrayDataFrameRows = Seq(Row(Seq(1.0, 2.0, 3.0)), Row(Seq(4.0, 5.0, 6.0)), Row(Seq(7.0, 8.0, 9.0)))
  val arrayDataFrameSchema = StructType(Seq(StructField("v", ArrayType(DoubleType, containsNull = false), nullable = false)))
  lazy val arrayDataFrame = createDataFrame(arrayDataFrameRows, arrayDataFrameSchema)

  "WriteDataFrame" should {

    "write CSV file without header" in {
      val wdf =
        new WriteDataFrame()
          .setStorageType(
            new OutputStorageTypeChoice.File()
              .setOutputFile(absoluteTestsDirPath.fullPath + "without-header")
              .setFileFormat(
                new OutputFileFormatChoice.Csv()
                  .setCsvColumnSeparator(CsvParameters.ColumnSeparatorChoice.Comma())
                  .setNamesIncluded(false)
              )
          )
      wdf.executeUntyped(List(dataframe))(executionContext)
      verifySavedDataFrame("without-header", rows, withHeader = false, ",")
    }

    "write CSV file with header" in {
      val wdf =
        new WriteDataFrame()
          .setStorageType(
            new OutputStorageTypeChoice.File()
              .setOutputFile(absoluteTestsDirPath.fullPath + "with-header")
              .setFileFormat(
                new OutputFileFormatChoice.Csv()
                  .setCsvColumnSeparator(CsvParameters.ColumnSeparatorChoice.Comma())
                  .setNamesIncluded(true)
              )
          )
      wdf.executeUntyped(List(dataframe))(executionContext)
      verifySavedDataFrame("with-header", rows, withHeader = true, ",")
    }

    "write CSV file with semicolon separator" in {
      val wdf =
        new WriteDataFrame()
          .setStorageType(
            new OutputStorageTypeChoice.File()
              .setOutputFile(absoluteTestsDirPath.fullPath + "semicolon-separator")
              .setFileFormat(
                new OutputFileFormatChoice.Csv()
                  .setCsvColumnSeparator(CsvParameters.ColumnSeparatorChoice.Semicolon())
                  .setNamesIncluded(false)
              )
          )
      wdf.executeUntyped(List(dataframe))(executionContext)
      verifySavedDataFrame("semicolon-separator", rows, withHeader = false, ";")
    }

    "write CSV file with colon separator" in {
      val wdf =
        new WriteDataFrame()
          .setStorageType(
            new OutputStorageTypeChoice.File()
              .setOutputFile(absoluteTestsDirPath.fullPath + "colon-separator")
              .setFileFormat(
                new OutputFileFormatChoice.Csv()
                  .setCsvColumnSeparator(CsvParameters.ColumnSeparatorChoice.Colon())
                  .setNamesIncluded(false)
              )
          )
      wdf.executeUntyped(List(dataframe))(executionContext)
      verifySavedDataFrame("colon-separator", rows, withHeader = false, ":")
    }

    // FIXME
    "write CSV file with space separator".is(pending)
//    in {
//      val wdf = WriteDataFrame(
//        columnSep(ColumnSeparator.SPACE),
//        writeHeader = false,
//        absoluteWriteDataFrameTestPath + "space-separator")
//      wdf.execute(List(dataframe))(executionContext)
//      verifySavedDataFrame("space-separator", rows, withHeader = false, " ")
//    }

    // This fails due to a bug in Spark-CSV
    "write CSV file with tab separator".is(pending)
//    in {
//      val wdf = WriteDataFrame(
//        columnSep(ColumnSeparator.TAB),
//        writeHeader = false,
//        absoluteWriteDataFrameTestPath + "tab-separator")
//      wdf.execute(List(dataframe))(executionContext)
//      verifySavedDataFrame("tab-separator", rows, withHeader = false, "\t")
//    }

    "write CSV file with custom separator" in {
      val wdf =
        new WriteDataFrame()
          .setStorageType(
            new OutputStorageTypeChoice.File()
              .setOutputFile(absoluteTestsDirPath.fullPath + "custom-separator")
              .setFileFormat(
                new OutputFileFormatChoice.Csv()
                  .setCsvColumnSeparator(
                    CsvParameters.ColumnSeparatorChoice
                      .Custom()
                      .setCustomColumnSeparator("X")
                  )
                  .setNamesIncluded(false)
              )
          )
      wdf.executeUntyped(List(dataframe))(executionContext)
      verifySavedDataFrame("custom-separator", rows, withHeader = false, "X")
    }

    "write ArrayType to Json" in {
      val wdf =
        new WriteDataFrame()
          .setStorageType(
            new OutputStorageTypeChoice.File()
              .setOutputFile(absoluteTestsDirPath.fullPath + "json-array")
              .setFileFormat(new OutputFileFormatChoice.Json())
          )
      wdf.executeUntyped(List(arrayDataFrame))(executionContext)
    }

    "throw an exception when writing ArrayType to CSV" in {
      val wdf =
        new WriteDataFrame()
          .setStorageType(
            new OutputStorageTypeChoice.File()
              .setOutputFile(absoluteTestsDirPath.fullPath + "csv-array")
              .setFileFormat(
                new OutputFileFormatChoice.Csv()
                  .setCsvColumnSeparator(CsvParameters.ColumnSeparatorChoice.Comma())
                  .setNamesIncluded(false)
              )
          )
      an[UnsupportedColumnTypeError] shouldBe thrownBy {
        wdf.executeUntyped(List(arrayDataFrame))(executionContext)
      }
    }

    "throw exception at inference time when using parquet with local driver files" in {
      val wdf = new WriteDataFrame()
        .setStorageType(
          new OutputStorageTypeChoice.File()
            .setOutputFile(File.pathPrefix + "/some_path/some_file.parquet")
            .setFileFormat(new OutputFileFormatChoice.Parquet())
        )

      an[ParquetNotSupported.type] shouldBe thrownBy {
        wdf.inferKnowledgeUntyped(List(Knowledge(dataframe)))(executionContext.inferContext)
      }
    }

    "throw exception at inference time when using invalid file scheme" in {
      val wdf = new WriteDataFrame()
        .setStorageType(
          new OutputStorageTypeChoice.File()
            .setOutputFile("invalidscheme://some_path/some_file.json")
            .setFileFormat(new OutputFileFormatChoice.Parquet())
        )

      an[UnknownFileSchemaForPath] shouldBe thrownBy {
        wdf.inferKnowledgeUntyped(List(Knowledge(dataframe)))(executionContext.inferContext)
      }
    }

    "overwrite file when it already exists and the overwrite param is set to true" in {
      val outputName = "some-name"
      val wdf =
        new WriteDataFrame()
          .setStorageType(
            new OutputStorageTypeChoice.File()
              .setOutputFile(absoluteTestsDirPath.fullPath + outputName)
              .setShouldOverwrite(true)
              .setFileFormat(
                new OutputFileFormatChoice.Csv()
                  .setCsvColumnSeparator(CsvParameters.ColumnSeparatorChoice.Comma())
                  .setNamesIncluded(false)
              )
          )
      wdf.executeUntyped(List(dataframe))(executionContext)
      val wdf1 =
        new WriteDataFrame()
          .setStorageType(
            new OutputStorageTypeChoice.File()
              .setOutputFile(absoluteTestsDirPath.fullPath + outputName)
              .setShouldOverwrite(true)
              .setFileFormat(
                new OutputFileFormatChoice.Csv()
                  .setCsvColumnSeparator(CsvParameters.ColumnSeparatorChoice.Comma())
                  .setNamesIncluded(true)
              )
          )
      wdf1.executeUntyped(List(dataframe))(executionContext)
      verifySavedDataFrame(outputName, rows, withHeader = true, ",")
    }

    "throw an exception when the file already exists and the overwrite param is set to false" in {
      val outputName = "some-name"
      val wdf =
        new WriteDataFrame()
          .setStorageType(
            new OutputStorageTypeChoice.File()
              .setOutputFile(absoluteTestsDirPath.fullPath + outputName)
              .setShouldOverwrite(false)
              .setFileFormat(
                new OutputFileFormatChoice.Csv()
                  .setCsvColumnSeparator(CsvParameters.ColumnSeparatorChoice.Comma())
                  .setNamesIncluded(false)
              )
          )
      wdf.executeUntyped(List(dataframe))(executionContext)
      val wdf1 =
        new WriteDataFrame()
          .setStorageType(
            new OutputStorageTypeChoice.File()
              .setOutputFile(absoluteTestsDirPath.fullPath + outputName)
              .setShouldOverwrite(false)
              .setFileFormat(
                new OutputFileFormatChoice.Csv()
                  .setCsvColumnSeparator(CsvParameters.ColumnSeparatorChoice.Comma())
                  .setNamesIncluded(true)
              )
          )
      a[HaranaIOError] shouldBe thrownBy {
        wdf1.executeUntyped(List(dataframe))(executionContext)
      }
    }
  }

  private def verifySavedDataFrame(savedFile: String, rows: Seq[Row], withHeader: Boolean, separator: String) = {

    val linesFromFile = Source
      .fromFile(absoluteTestsDirPath.pathWithoutScheme + savedFile)
      .getLines()
      .toArray

    val lines = if (withHeader) linesFromFile.tail else linesFromFile

    if (withHeader) {
      val headersLine = linesFromFile.head
      headersLine shouldBe schema.fieldNames.mkString(s"$separator")
    }

    for (idx <- rows.indices) {
      val got = lines(idx)
      val expected = rowsAsCsv(separator)(idx)
      got shouldEqual expected
    }
  }
}
