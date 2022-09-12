package com.harana.sdk.shared.models.flow

import enumeratum.{CirceEnum, Enum, EnumEntry}
import io.circe.generic.JsonCodec

sealed trait DataType extends EnumEntry
case object DataType extends Enum[DataType] with CirceEnum[DataType] {
  case object StringType extends DataType
  case object DoubleType extends DataType
  case object TimestampType extends DataType
  case object BooleanType extends DataType
  case object IntegerType extends DataType
  case object FloatType extends DataType
  case object LongType extends DataType
  val values = findValues
}

@JsonCodec
case class StructType(fields: Array[StructField]) extends DataType with Seq[StructField] {
  override def length: Int = fields.length
  override def iterator: Iterator[StructField] = fields.iterator
  override def apply(fieldIndex: Int): StructField = fields(fieldIndex)
}

@JsonCodec
case class StructField(name: String,
                       dataType: DataType,
                       nullable: Boolean = true,
                       metadata: Metadata = Metadata.empty)

@JsonCodec
case class Metadata (map: Map[String, String]) extends Serializable

object Metadata {
  def empty: Metadata = new Metadata(Map.empty)
}