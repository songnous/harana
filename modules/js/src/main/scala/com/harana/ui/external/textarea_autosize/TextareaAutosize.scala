package com.harana.ui.external.textarea_autosize

import org.scalajs.dom.HTMLTextAreaElement
import slinky.core.ExternalComponent
import slinky.core.annotations.react
import typings.react.mod.ChangeEvent

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport

@JSImport("react-textarea-autosize", JSImport.Default)
@js.native
object ReactTextareaAutosize extends js.Object

@react object TextareaAutosize extends ExternalComponent {

  case class Props(inputRef: Option[HTMLTextAreaElement => Unit] = None,
                   maxRows: Option[Double] = None,
                   minRows: Option[Double] = None,
                   onChange: Option[ChangeEvent[HTMLTextAreaElement] => Unit] = None,
                   onHeightChange: Option[Double => Unit] = None,
                   useCacheForDOMMeasurements: Option[Boolean] = None,
                   value: Option[String] = None)

  override val component = ReactTextareaAutosize
}