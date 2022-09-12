package com.harana.ui.external.input_mask

import org.scalablytyped.runtime.StringDictionary
import slinky.core.ExternalComponent
import slinky.core.annotations.react
import typings.react.mod.Ref
import typings.std.{HTMLInputElement, Record}

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport
import scala.scalajs.js.|

@JSImport("react-input-mask", JSImport.Namespace)
@js.native
object ReactInputMask extends js.Object

@react object InputMask extends ExternalComponent {

  case class Props(mask: String,
                   className: Option[String] = None,
                   placeholder: Option[String] = None,
                   alwaysShowMask: Option[Boolean] = None,
                   beforeMaskedValueChange: Option[(InputState, InputState, String, MaskOptions) => InputState] = None,
                   formatChars: Option[StringDictionary[String]] = None,
                   inputRef: Option[Ref[HTMLInputElement]] = None,
                   maskChar: Option[String | Null] = None)

  override val component = ReactInputMask
}

case class InputState(selection: Selection | Null, value: String)

case class MaskOptions(alwaysShowMask: Boolean,
                       formatChars: Record[String,String],
                       mask: String,
                       maskChar: String,
                       permanents: List[Double])

case class Selection(end: Double, start: Double)