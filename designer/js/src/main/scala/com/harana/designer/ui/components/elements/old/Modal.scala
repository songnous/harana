package com.harana.ui.components.elements.old

import com.harana.ui.components.{LinkType, literal, when}
import enumeratum.values.{StringCirceEnum, StringEnum, StringEnumEntry}
import slinky.core.StatelessComponent
import slinky.core.annotations.react
import slinky.core.facade.ReactElement
import slinky.web.html.{div, style, _}

import scala.scalajs.js

@react class Modal extends StatelessComponent {

  case class Props(id: String,
                   title: String,
                   resizable: Option[Boolean] = None,
                   alsoResizable: Option[String] = None,
									 size: Option[ModalSize] = None,
									 cancelButtonTitle: Option[String] = None,
									 topRight: Option[ReactElement] = None,
									 bottomLeft: Option[ReactElement] = None,
                   bottomRight: Option[ReactElement] = None,
                   children: ReactElement)

  override def componentDidMount() = {
    if (props.resizable.getOrElse(false)) {
      val modal = js.Dynamic.global.jQuery(s"#${props.id} .modal-content")
      modal.resizable()
      modal.resizable("option", "autoResize", s".modal-header, .modal-body, .modal-footer, #${props.alsoResizable.getOrElse("")}")
    }
  }

  val isFull = props.size.nonEmpty && props.size.get.equals(ModalSize.Full)
  val fullscreenContentStyle = if (isFull) literal("width" -> "100%", "height" -> "92%", "display" -> "table") else literal()
  val fullscreenBodyStyle = if (isFull) literal("width" -> "100%", "height" -> "100%", "display" -> "table-row") else literal()

	def render() =
		div(id := props.id, className := s"modal fade", tabIndex := -1)(
			div(className := s"modal-dialog ${props.size.getOrElse(ModalSize.Small).value}")(
				div(className := "modal-content", style := fullscreenContentStyle)(
					div(className := "modal-header", style := literal("padding" -> "10px", "height" -> "60px"))(
						div(className := "row", style := literal("paddingTop" -> "2px"))(
							div(className := "col-md-4")(
								h5(className := "modal-title", style := literal("padding" -> "5px 0px 0px 10px"))(props.title)
							),
							div(className := "col-md-8", style := literal("textAlign" -> "right"))(
								div(when(props.topRight, props.topRight.get))
							)
						)
					),
					div(className := "modal-body", style := fullscreenBodyStyle)(
						props.children
					),
					div(className := "modal-footer", style := literal("textAlign" -> "left", "padding" -> "10px", "height" -> "60px"))(
						div(className := "row")(
							div(className := "col-md-6")(
								div(when(props.bottomLeft, props.bottomLeft.get)),
							),
							div(className := "col-md-6", style := literal("textAlign" -> "right"))(
								div(when(props.bottomRight, props.bottomRight.get)),
								//Button(Some(props.cancelButtonTitle.getOrElse("Cancel")), LinkType.HideModal)
							)
						)
          )
				)
			)
		)

  def hide = {
    js.Dynamic.global.jQuery(s"#${props.id}").modal("hide")
  }
}

sealed abstract class ModalSize(val value: String) extends StringEnumEntry
case object ModalSize extends StringEnum[ModalSize] with StringCirceEnum[ModalSize] {
	case object ExtraSmall extends ModalSize("modal-xs")
	case object Small extends ModalSize("modal-sm")
	case object Large extends ModalSize("modal-lg")
	case object Full extends ModalSize("modal-full")
	val values = findValues
}