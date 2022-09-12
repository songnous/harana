package com.harana.ui.external.input_autosize

import org.scalajs.dom.HTMLInputElement
import slinky.core.ExternalComponent
import slinky.core.annotations.react
import typings.react.mod.{CSSProperties, Ref}

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport
import scala.scalajs.js.|

@JSImport("react-input-autosize", JSImport.Namespace)
@js.native
object ReactInputAutosize extends js.Object {
  def getInput(): HTMLInputElement = js.native
}

@react object InputAutosize extends ExternalComponent {

  case class Props(inputClassName: Option[String] = None,
                   inputRef: Option[Ref[HTMLInputElement]] = None,
                   inputStyle: Option[CSSProperties] = None,
                   minWidth: Option[String | Double] = None,
                   onAutosize: Option[String | Double => Unit] = None,
                   placeholderIsMinWidth: Option[Boolean] = None)

  override val component = ReactInputAutosize
}