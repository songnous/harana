package com.harana.modules.dremio.models

import io.circe._
import io.circe.parser._
import io.circe.syntax._
import io.circe.generic.auto._
import org.latestbit.circe.adt.codec._

sealed trait DatasetFormat
object DatasetFormat {

  implicit val encoder : Encoder[DatasetFormat] = JsonTaggedAdtCodec.createEncoder[DatasetFormat]("type")
  implicit val decoder : Decoder[DatasetFormat] = JsonTaggedAdtCodec.createDecoder[DatasetFormat]("type")

  case class Excel(`type`: String,
                   sheetName: String,
                   extractHeader: Boolean,
                   hasMergedCells: Boolean) extends DatasetFormat

  case class JSON(`type`: String) extends DatasetFormat

  case class Parquet(`type`: String) extends DatasetFormat

  case class Text(`type`: Text,
                  fieldDelimiter: String,
                  lineDelimiter: String,
                  quote: String,
                  comment: String,
                  escape: String,
                  skipFirstLine: Boolean,
                  extractHeader: Boolean,
                  trimHeader: Boolean,
                  autoGenerateColumnNames: Boolean) extends DatasetFormat

  case class XLS(`type`: String,
                 sheetName: String,
                 extractHeader: Boolean,
                 hasMergedCells: Boolean) extends DatasetFormat
}
