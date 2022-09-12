package com.harana.ui.external.select2

import slinky.core.ExternalComponent
import slinky.core.annotations.react

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport
import scala.scalajs.js.|

@JSImport("react-select2-wrapper", JSImport.Namespace)
@js.native
object ReactSelect2 extends js.Object

@react object Select2 extends ExternalComponent {

  case class Props(data: List[String] | List[Select2Item],
                   options: Select2Options = Select2Options("Select .."),
                   className: Option[String] = None,
                   defaultValue: Option[String | List[String]] = None,
                   value: Option[String] = None,
                   multiple: Boolean = false,
                   onOpen: () => Unit = () => {},
                   onClose: () => Unit = () => {},
                   onSelect: OnChange => Unit = OnChange => {},
                   onChange: OnChange => Unit = OnChange => {},
                   onUnselect: () => Unit = () => {})

  override val component = ReactSelect2
}

trait Select2Item extends js.Object {
  val text: String
  val id: Option[String]
  val selected: Boolean
  val disabled: Boolean
  val children: List[String]
}

case class Select2Options(placeholder: String,
                          allowClear: Boolean = false,
                          width: Option[String] = None)

@js.native
trait OnChange extends js.Object {
  val params: OnChangeParams
}

@js.native
trait OnChangeParams extends js.Object {
  val data: OnChangeItem
}

@js.native
trait OnChangeItem extends js.Object {
  val text: String
  val id: String
  val title: String
  val selected: Boolean
  val disabled: Boolean
}
