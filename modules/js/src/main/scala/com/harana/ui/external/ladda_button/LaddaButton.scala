package com.harana.ui.external.ladda_button

import slinky.core.ExternalComponent
import slinky.core.annotations.react

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport

@JSImport("react-ladda", JSImport.Default)
@js.native
object ReactLadda extends js.Object

@react object LaddaButton extends ExternalComponent {

  case class Props(loading: Boolean = true,
                   progress: Double = 0.6,
                   `data-color`: String = "#abcabc",
                   `data-size`: String = "S",
                   `data-style`: String = "SLIDE_RIGHT",
                   `data-spinner-size`: Int = 30,
                   `data-spinner-color`: String = "#dddfff",
                   `data-spinner-lines`: Int = 12)

  override val component = ReactLadda
}