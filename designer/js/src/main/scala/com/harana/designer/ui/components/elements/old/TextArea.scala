package com.harana.ui.components.elements.old

import com.harana.ui.components.emptyFn
import org.scalajs.dom.ext.KeyCode
import slinky.core.StatelessComponent
import slinky.core.annotations.react
import slinky.web.html.div
import typings.react.mod.{ChangeEvent, FocusEvent, KeyboardEvent}
import typings.std.HTMLTextAreaElement

@react class TextArea extends StatelessComponent {

	case class Props(name: String,
									 rows: Int,
									 columns: Int,
									 defaultValue: Option[String] = None,
									 fillToContainer: Boolean = false,
									 growVertically: Boolean = false,
									 onBlur: String => Unit = emptyFn[String],
									 onChange: String => Unit = emptyFn[String],
									 onFocus: String => Unit = emptyFn[String],
									 onKeyDown: String => Unit = emptyFn[String],
									 onEnter: String => Unit = emptyFn[String],
									 onEscape: String => Unit = emptyFn[String],
									 placeholder: Option[String] = None,
									 small: Boolean = false,
									 value: Option[String] = None)

	def render() =
		div()
//		 BPTextArea(
//			 name = Some(props.name),
//			 rows = Some(props.rows),
//			 cols = Some(props.columns),
//			 defaultValue = props.defaultValue,
//			 fill = Some(props.fillToContainer),
//			 growVertically = Some(props.growVertically),
//			 placeholder = props.placeholder,
//			 onBlur = Some({ e: FocusEvent[HTMLTextAreaElement] => props.onBlur(e.currentTarget.value) }),
//			 onChange = Some({ e: ChangeEvent[HTMLTextAreaElement] => props.onChange(e.currentTarget.value) }),
//			 onFocus = Some({ e: FocusEvent[HTMLTextAreaElement]  => props.onFocus(e.currentTarget.value) }),
//			 onKeyDown = Some({ e: KeyboardEvent[HTMLTextAreaElement] =>
//				 val value = e.currentTarget.value
//				 e.keyCode match {
//					 case KeyCode.Enter => props.onEnter(value)
//					 case KeyCode.Escape => props.onEscape(value)
//					 case _ => props.onKeyDown(value)
//				 }
//			 }),
//			 small = Some(props.small),
//			 value = props.value
//		 )
}