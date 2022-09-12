package com.harana.ui.external.simplemaps

import slinky.core.ExternalComponent
import slinky.core.annotations.react

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport

@JSImport("react-simple-maps", "Markers")
@js.native
object ReactMarkers extends js.Object

@react object Markers extends ExternalComponent {

  type Props = Unit

  override val component = ReactMarkers
}