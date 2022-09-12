package com.harana.ui.external.simplemaps

import slinky.core.ExternalComponent
import slinky.core.annotations.react

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport

@JSImport("react-simple-maps", "Annotations")
@js.native
object ReactAnnotations extends js.Object

@react object Annotations extends ExternalComponent {

  type Props = Unit

  override val component = ReactAnnotations
}