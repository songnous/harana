package com.harana.ui.external.table

import slinky.core.ExternalComponent
import slinky.core.annotations.react

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport

@JSImport("react-bootstrap-table", JSImport.Default)
@js.native
object ReactBootstrapTable extends js.Object

@react object Table extends ExternalComponent {

  case class Props()

  override val component = ReactBootstrapTable
}