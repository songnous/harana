package com.harana.sdk.backend.models.flow.actions.readwritedataframe.filestorage.csv

import com.harana.sdk.backend.models.flow.ExecutionContext
import com.harana.sdk.backend.models.flow.actionobjects.dataframe.DataFrame
import com.harana.sdk.backend.models.flow.actions.exceptions.UnsupportedColumnTypeError
import org.apache.spark.sql.Row
import org.apache.spark.sql.types._
import java.time.Instant

import java.sql.Timestamp

/** In CSV there are no type hints/formats. Everything is plain text between separators.
  *
  * That's why it's needed to convert all fields to string and make sure that there are no nested structures like Maps
  * or Arrays.
  */
object CsvSchemaStringifierBeforeCsvWriting {

  def preprocess(dataFrame: DataFrame)(implicit context: ExecutionContext) = {
    requireNoComplexTypes(dataFrame)

    val schema                                                 = dataFrame.sparkDataFrame.schema
    def stringifySelectedTypes(schema: StructType): StructType = {
      StructType(
        schema.map { field: StructField => field.copy(dataType = StringType) }
      )
    }

    context.dataFrameBuilder.buildDataFrame(
      stringifySelectedTypes(schema),
      dataFrame.sparkDataFrame.rdd.map(stringifySelectedCells(schema))
    )
  }

  private def requireNoComplexTypes(dataFrame: DataFrame) = {
    dataFrame.sparkDataFrame.schema.fields.map(structField => (structField.dataType, structField.name)).foreach {
      case (dataType, columnName) =>
        dataType match {
          case _: ArrayType | _: MapType | _: StructType => throw UnsupportedColumnTypeError(columnName, dataType).toException
          case _                                         => ()
        }
    }
  }

  private def stringifySelectedCells(originalSchema: StructType)(row: Row): Row = {
    Row.fromSeq(row.toSeq.zipWithIndex.map { case (value, index) =>
      (value, originalSchema(index).dataType) match {
        case (null, _) => ""
        case (_, BooleanType)   => if (value.asInstanceOf[Boolean]) "1" else "0"
        case (_, TimestampType) => Instant.ofEpochMilli(value.asInstanceOf[Timestamp].getTime).toString
        case (x, _)             => value.toString
      }
    })
  }
}
