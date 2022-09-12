package com.harana.sdk.backend.models.flow.actions.readwritedataframe.filestorage.csv

import com.harana.sdk.shared.models.flow.actions.inout.CsvParameters.ColumnSeparatorChoice
import com.harana.sdk.shared.models.flow.actions.inout.CsvParameters
import org.apache.spark.sql.DataFrameReader

object CsvOptions {

  def map(namesIncluded: Boolean, columnSeparator: ColumnSeparatorChoice): Map[String, String] = {
    val headerFlag = if (namesIncluded) "true" else "false"
    Map(
      "header" -> headerFlag,
      "delimiter"   -> CsvParameters.determineColumnSeparatorOf(columnSeparator).toString,
      "inferSchema" -> "false"
    )
  }

  // Unfortunately, making analogous RichDataFrameWriter is awkward, if not impossible.
  // This is because between Spark 1.6 and 2.0 DataFrameWriter became parametrized
  implicit class RichDataFrameReader(self: DataFrameReader) {

    def setCsvOptions(namesIncluded: Boolean, columnSeparator: ColumnSeparatorChoice): DataFrameReader = {
      val paramMap = map(namesIncluded, columnSeparator)
      paramMap.foldLeft(self) { case (reader, (key, value)) =>
        reader.option(key, value)
      }
    }
  }
}