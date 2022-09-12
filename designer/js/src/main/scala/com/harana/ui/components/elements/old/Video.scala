package com.harana.ui.components.elements.old

import com.harana.ui.components.Url
import enumeratum.values.{StringEnum, StringEnumEntry}
import enumeratum.{Enum, EnumEntry}
import slinky.core.StatelessComponent
import slinky.core.annotations.react
import slinky.web.html._

@react class Video extends StatelessComponent {

	case class Props(url: Url,
									 embedMethod: VideoEmbedMethod = VideoEmbedMethod.Embed,
									 aspectRatio: VideoAspectRatio = VideoAspectRatio.SixteenByNine)

	def render() =
		div(className := s"embed-responsive embed-responsive-${props.aspectRatio.value}")(
			props.embedMethod match {
				case VideoEmbedMethod.iFrame => iframe(className := "embed-responsive-item", src := props.url)
				case VideoEmbedMethod.Embed => embed(className := "embed-responsive-item", src := props.url)
				case VideoEmbedMethod.Object => `object`(className := "embed-responsive-item", data := props.url)
			}
		)
}

sealed trait VideoEmbedMethod extends EnumEntry
case object VideoEmbedMethod extends Enum[VideoEmbedMethod] {
	case object iFrame extends VideoEmbedMethod
	case object Embed extends VideoEmbedMethod
	case object Object extends VideoEmbedMethod
	val values = findValues
}

sealed abstract class VideoAspectRatio(val value: String) extends StringEnumEntry
case object VideoAspectRatio extends StringEnum[VideoAspectRatio] {
	case object SixteenByNine extends VideoAspectRatio("16by9")
	case object FourByThree extends VideoAspectRatio("4by3")
	val values = findValues
}