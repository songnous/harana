package com.harana.ui.external.tags_input

import com.harana.ui.external.tags_input.Types.Tag
import org.scalablytyped.runtime.StringDictionary
import slinky.core.ExternalComponent
import slinky.core.annotations.react
import slinky.core.facade.ReactElement
import typings.react.mod.{ChangeEvent, Component, ReactChild}
import typings.std.RegExp

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport
import scala.scalajs.js.|

@JSImport("react-tagsinput", JSImport.Default)
@js.native
object ReactTagsInput extends js.Object {
  def accept(): js.Any = js.native
  def addTag(tag: Tag): js.Any = js.native
  def blur(): Unit = js.native
  def clearInput(): Unit = js.native
  def focus(): Unit = js.native
}

@react object TagsInput extends ExternalComponent {

  case class Props(addKeys: List[Double] = List(),
                   addOnBlur: Option[Boolean] = None,
                   addOnPaste: Option[Boolean] = None,
                   className: Option[String] = None,
                   currentValue: Option[String] = None,
                   disabled: Option[Boolean] = None,
                   focusedClassName: Option[String] = None,
                   inputProps: Option[StringDictionary[js.Any]] = None,
                   inputValue: Option[String] = None,
                   maxTags: Option[Double] = None,
                   onChangeInput: Option[String => Unit] = None,
                   onValidationReject: Option[String => Unit] = None,
                   onlyUnique: Option[Boolean] = None,
                   pasteSplit: Option[String => List[String]] = None,
                   preventSubmit: Option[Boolean] = None,
                   removeKeys: List[Double] = List(),
                   renderInput: Option[RenderInputProps => ReactElement] = None,
                   renderLayout: Option[(List[Component[js.Object, js.Object, _]], Component[js.Object, js.Object, _]) => ReactChild] = None,
                   renderTag: Option[RenderTagProps => ReactElement] = None,
                   tagDisplayProp: Option[String] = None,
                   tagProps: Option[TagProps] = None,
                   validationRegex: Option[RegExp] = None,
                   value: List[Tag],
                   onChange: (List[Tag], List[Tag], List[Double]) => Unit)

  override val component = ReactTagsInput
}

object Types {
  type Tag = js.Any
}

trait TagProps extends StringDictionary[js.Any]

trait RenderInputProps extends StringDictionary[js.Any] {
  val value: Tag
  def addTag(tag: Tag): Unit
  def onChange(e: ChangeEvent[Value]): Unit
  def ref(r: js.Any): Unit
}

trait RenderTagProps extends TagProps {
  val disabled: Boolean
  val tag: Tag
  def getTagDisplayValue(tag: Tag): String
  def onRemove(tagIndex: Double): Unit
}

case class Value(value: String)