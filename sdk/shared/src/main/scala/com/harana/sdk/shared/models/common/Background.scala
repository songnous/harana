package com.harana.sdk.shared.models.common

import io.circe.generic.JsonCodec
import com.harana.sdk.shared.utils.CirceCodecs._

sealed trait Background
object Background {

    @JsonCodec
    case class Gradient(colors: List[String],
                        direction: Option[String] = scala.None) extends Background

    @JsonCodec
    case class Hex(hex: String) extends Background

	  @JsonCodec
    case class HSL(hue: String, saturation: String, lightness: String, alpha: String) extends Background
    
    @JsonCodec
    case class Image(url: String) extends Background

	  @JsonCodec
    case class RGB(red: String, green: String, blue: String, alpha: String) extends Background
}