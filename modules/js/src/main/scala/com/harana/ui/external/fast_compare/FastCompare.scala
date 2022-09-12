package com.harana.ui.external.fast_compare

import slinky.core.ExternalComponent
import slinky.core.annotations.react

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport

@JSImport("react-fast-compare", JSImport.Namespace)
@js.native
object ReactFastCompare extends js.Object {
  def apply(a: js.Any, b: js.Any): Boolean = js.native
}

@react object FastCompare extends ExternalComponent {
  type Props = Unit
  override val component = ReactFastCompare
}