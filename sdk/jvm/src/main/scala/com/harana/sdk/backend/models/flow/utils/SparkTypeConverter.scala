package com.harana.sdk.backend.models.flow.utils

import com.harana.sdk.shared.models.flow.DataType.{BooleanType, DoubleType, FloatType, IntegerType, LongType, StringType, TimestampType}
import org.apache.spark.mllib.linalg.SparseVector
import org.apache.spark.mllib.linalg.Vector
import org.apache.spark.mllib.linalg.Vectors
import org.apache.spark.sql.Row
import org.apache.spark.sql.types.{DataType => SparkDataType, StructType => SparkStructType}
import com.harana.sdk.shared.models.designer.flow._
import com.harana.sdk.shared.models.flow.DataType._
import com.harana.sdk.shared.models.flow.{DataType, Metadata, StructField, StructType}
import com.harana.sdk.shared.models.flow.utils.{ColumnType, DoubleUtils}
import io.circe.parser
import org.apache.spark.sql.types.{DataType => SparkDataType, StructType => SparkStructType}
import io.circe.parser._

import scala.util.Try

object SparkTypeConverter {

  val defaultLimitInSeq = 20

  def getColumnAsDouble(columnIndex: Int)(row: Row) =
    getOption(columnIndex)(row).map(SparkTypeConverter.sparkAnyToDouble).getOrElse(Double.NaN)

  def getColumnAsString(columnIndex: Int)(row: Row) =
    getOption(columnIndex)(row).map(SparkTypeConverter.sparkAnyToString).getOrElse("NULL")

  def rowToDoubleVector(row: Row): Vector = {
    val values = (0 until row.size).map(columnIndex => getOption(columnIndex)(row).map(SparkTypeConverter.sparkAnyToDouble).getOrElse(Double.NaN))
    Vectors.dense(values.toArray)
  }

  def getOption(column: Int)(row: Row) = if (row.isNullAt(column)) None else Some(row.get(column))

  def cellToString(row: Row, index: Int): Option[String] =
    if (row.isNullAt(index))
      None
    else {
      val sparkAny = row.get(index)
      Some(sparkAnyToString(sparkAny))
    }

  def cellToDouble(row: Row, column: Int): Option[Double] =
    if (row.isNullAt(column))
      None
    else {
      val sparkAny = row.get(column)
      Some(sparkAnyToDouble(sparkAny))
    }

  def sparkAnyToString(value: Any): String = {
    value match {
      case sparseVector: SparseVector    => sparseVectorToString(sparseVector)
      case vector: Vector                => sparkAnyToString(vector.toArray)
      case array: Array[_]               => sparkAnyToString(array.toSeq)
      case seq: Seq[_]                   => seqToString(seq)
      case (key, tupleValue)             => s"(${sparkAnyToString(key)}, ${sparkAnyToString(tupleValue)})"
      case float: java.lang.Float        => DoubleUtils.double2String(float.toDouble)
      case double: java.lang.Double      => DoubleUtils.double2String(double)
      case decimal: java.math.BigDecimal => decimal.toPlainString
      case timestamp: java.sql.Timestamp => timestamp.getTime.toString
      case date: java.sql.Date           => date.getTime.toString
      case string: String                => string
      case other                         => other.toString
    }
  }

  def sparkAnyToDouble(value: Any): Double = {
    value match {
      case bool: java.lang.Boolean       => if (bool) 1d else 0d
      case n: Number                     => n.doubleValue()
      case date: java.sql.Date           => dateToDouble(date)
      case timestamp: java.sql.Timestamp => timestamp.getTime.toDouble
      case other                         => Double.NaN
    }
  }

  private def dateToDouble(date: java.sql.Date): Double =
    date.getTime.toDouble

  private def seqToString(seq: Seq[_], optionalLimit: Option[Int] = Some(defaultLimitInSeq)) = {
    val moreValuesMark = "..."
    val itemsToString  = optionalLimit match {
      case None        => seq
      case Some(limit) => if (seq.length > limit) seq.take(limit) ++ Seq(moreValuesMark) else seq
    }
    itemsToString.map(sparkAnyToString).mkString("[", ", ", "]")
  }

  private def sparseVectorToString(sparseVector: SparseVector) = {
    val size    = sparseVector.size
    val indices = sparseVector.indices.toSeq
    val values  = sparseVector.values.toSeq
    val pairs   = indices.zip(values)

    s"($size, ${sparkAnyToString(pairs)})"
  }

  def sparkColumnTypeToColumnType(sparkColumnType: SparkDataType): ColumnType = {
    sparkColumnType match {

      case _: org.apache.spark.sql.types.IntegerType      => ColumnType.Integer
      case _: org.apache.spark.sql.types.FloatType        => ColumnType.Float
      case _: org.apache.spark.sql.types.DoubleType       => ColumnType.Double
      case _: org.apache.spark.sql.types.NumericType      => ColumnType.Numeric
      case _: org.apache.spark.sql.types.StringType       => ColumnType.String
      case _: org.apache.spark.sql.types.BooleanType      => ColumnType.Boolean
      case _: org.apache.spark.sql.types.TimestampType    => ColumnType.Timestamp
      case _: org.apache.spark.sql.types.ArrayType        => ColumnType.Array
      case _: com.harana.spark.Linalg.VectorUDT           => ColumnType.Vector
      case _                                              => ColumnType.Other
    }
  }

  def toSparkDataType(dataType: DataType): SparkDataType = {
    dataType match {
      case StringType => org.apache.spark.sql.types.StringType
      case DoubleType => org.apache.spark.sql.types.DoubleType
      case TimestampType => org.apache.spark.sql.types.TimestampType
      case BooleanType => org.apache.spark.sql.types.BooleanType
      case IntegerType => org.apache.spark.sql.types.IntegerType
      case FloatType => org.apache.spark.sql.types.FloatType
      case LongType => org.apache.spark.sql.types.LongType
    }
  }

  def fromSparkDataType(dataType: SparkDataType): DataType = {
    dataType match {
      case org.apache.spark.sql.types.StringType => StringType
      case org.apache.spark.sql.types.DoubleType => DoubleType
      case org.apache.spark.sql.types.TimestampType => TimestampType
      case org.apache.spark.sql.types.BooleanType => BooleanType
      case org.apache.spark.sql.types.IntegerType => IntegerType
      case org.apache.spark.sql.types.FloatType => FloatType
      case org.apache.spark.sql.types.LongType => LongType
    }
  }

  def fromSparkStructType(structType: SparkStructType): StructType =
    StructType(
      structType.fields.map(f => {
        val map = parser.parse(f.metadata.json).toTry
          .flatMap(json => Try(json.asObject.getOrElse(sys.error("Not a JSON Object"))))
          .flatMap(jsonObject => Try(jsonObject.toMap.map { case (name, value) => name ->
            value.asString.getOrElse(sys.error(s"Field '$name' is not a JSON string"))
          }))
          .getOrElse(Map.empty[String, String])
        StructField(f.name, fromSparkDataType(f.dataType), f.nullable, new Metadata(map))
      })
    )
}
