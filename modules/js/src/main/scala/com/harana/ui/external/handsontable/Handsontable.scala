package com.harana.ui.external.handsontable

import slinky.core.ExternalComponent
import slinky.core.annotations.react

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport

@JSImport("@handsontable/react", "HotTable")
@js.native
object ReactHandsontable extends js.Object

@react object Handsontable extends ExternalComponent {

  case class Props(data: List[js.Object] = List(),
                   id: Option[String] = None,
                   className: Option[String] = None,
                   style: Option[js.Object] = None,
                   settings: Option[js.Object] = None)

  override val component = ReactHandsontable
}