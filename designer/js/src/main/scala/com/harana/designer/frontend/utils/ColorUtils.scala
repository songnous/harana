package com.harana.designer.frontend.utils

import com.harana.sdk.shared.models.common.Background
import scala.scalajs.js.Dynamic.literal

import scala.util.Random

object ColorUtils {

    def css(background: Background) =
	    background match {
				case Background.Gradient(colors, direction) => literal("backgroundImage" -> s"linear-gradient($direction, ${colors.mkString(",")}")
		    case Background.Hex(hex) => literal("backgroundColor" -> s"#$hex")
		    case Background.RGB(red, green, blue, alpha) => literal("backgroundColor" -> s"rgba($red, $green, $blue, $alpha)")
		    case Background.HSL(hue, saturation, lightness, alpha) => literal("backgroundColor" -> s"hsla($hue%, $saturation%, $lightness%, $alpha)")
		    case Background.Image(url) => literal("backgroundImage" -> s"""url("$url")""")
	    }

    def randomBackground =
      s"/public/images/pills/pill${Random.nextInt(96)+1}.jpg"
}
