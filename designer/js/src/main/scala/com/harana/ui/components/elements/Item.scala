package com.harana.ui.components.elements

import com.harana.ui.external.shoelace.{Button => ShoelaceButton, Icon => ShoelaceIcon}
import enumeratum.values.{StringCirceEnum, StringEnum, StringEnumEntry}
import slinky.core.StatelessComponent
import slinky.core.annotations.react
import slinky.core.facade.ReactElement
import slinky.web.html._

@react class Item extends StatelessComponent {

	case class Props(title: Option[String],
									 item: Option[(ItemType, ItemPosition)] = None)

	def render() =
		props.item match {
			case Some(item) =>
				val position = item._2
				val content: ReactElement = item._1 match {
					case ItemType.Badge(color, number) => span(className := s"badge badge-$color")(number)
					case ItemType.Button(button) => ShoelaceButton(button)
					case ItemType.Icon(library, icon) => ShoelaceIcon(library = Some(library), name = icon)
					case ItemType.Label(color, number) => span(className := s"label label-$color ${position.value}")(number)
				}
				position match {
					case ItemPosition.Left => List[ReactElement](content, props.title)
					case ItemPosition.Right => List[ReactElement](props.title, content)
				}
			case None => props.title.getOrElse("").asInstanceOf[ReactElement]
		}
}

sealed trait ItemType
object ItemType {
	case class Badge(color: Color, number: Int) extends ItemType
	case class Button(button: ShoelaceButton.Props) extends ItemType
	case class Icon(library: String, icon: String) extends ItemType
	case class Label(color: Color, text: String) extends ItemType
}

sealed abstract class ItemPosition(val value: String) extends StringEnumEntry
case object ItemPosition extends StringEnum[ItemPosition] with StringCirceEnum[ItemPosition] {
	case object Left extends ItemPosition("position-left")
	case object Right extends ItemPosition("position-right")
	val values = findValues
}