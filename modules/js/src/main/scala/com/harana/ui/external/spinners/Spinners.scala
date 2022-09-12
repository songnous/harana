package com.harana.ui.external.spinners

import slinky.core.ExternalComponent
import slinky.core.annotations.react

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport


@JSImport("react-spinners", JSImport.Namespace)
@js.native
object ReactSpinners extends js.Object {
  val CircleLoader: js.Object = js.native
  val ScaleLoader: js.Object = js.native
}

@react object CircleLoader extends ExternalComponent {
  case class Props(size: Int, color: Option[String])
  val component = ReactSpinners.CircleLoader
}

@react object ScaleLoader extends ExternalComponent {
  case class Props(height: Int, width: Int, margin: String, radius: Int)
  val component = ReactSpinners.ScaleLoader
}