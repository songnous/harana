package com.harana.ui.external.simplemaps

import slinky.core.ExternalComponent
import slinky.core.annotations.react

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport

@JSImport("react-simple-maps", "Lines")
@js.native
object ReactLines extends js.Object

@react object Lines extends ExternalComponent {

  type Props = Unit

  override val component = ReactLines
}