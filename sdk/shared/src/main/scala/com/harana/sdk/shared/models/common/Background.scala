package com.harana.sdk.shared.models.common

import io.circe.{Decoder, Encoder}
import io.circe.generic.auto._
import org.latestbit.circe.adt.codec._

sealed trait Background
object Background {
  case class Gradient(colors: List[String], direction: Option[String] = scala.None) extends Background
  case class Hex(hex: String) extends Background
  case class HSL(hue: String, saturation: String, lightness: String, alpha: String) extends Background
  case class Image(url: String) extends Background
  case class RGB(red: String, green: String, blue: String, alpha: String) extends Background

  implicit val encoder: Encoder[Background] = JsonTaggedAdtCodec.createEncoder[Background]("type")
  implicit val decoder: Decoder[Background] = JsonTaggedAdtCodec.createDecoder[Background]("type")

}