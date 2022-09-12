package com.harana.ui.external.datasheet

import slinky.core.ExternalComponent
import slinky.core.annotations.react

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport

@JSImport("react-datasheet", JSImport.Default)
@js.native
object ReactDatasheet extends js.Object

@react object Datasheet extends ExternalComponent {

  case class Props()

  override val component = ReactDatasheet
}