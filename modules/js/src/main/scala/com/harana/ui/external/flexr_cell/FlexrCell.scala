package com.harana.ui.external.flexr_cell

import slinky.core.ExternalComponent
import slinky.core.annotations.react

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport
import scala.scalajs.js.|

@JSImport("react-flexr", "Cell")
@js.native
object ReactFlexrCell extends js.Object

@react object FlexrCell extends ExternalComponent {

  case class Props(align: Option[String],
                   desk: Option[String | Double] = None,
                   flex: Option[Boolean] = None,
                   gutter: Option[String] = None,
                   lap: Option[String | Double] = None,
                   palm: Option[String | Double] = None,
                   portable: Option[String | Double] = None,
                   size: Option[String | Double] = None)

  override val component = ReactFlexrCell
}