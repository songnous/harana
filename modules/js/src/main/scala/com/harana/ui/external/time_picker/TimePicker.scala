package com.harana.ui.external.time_picker

import slinky.core.ExternalComponent
import slinky.core.annotations.react

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport

@JSImport("react-bootstrap-time-picker", JSImport.Default)
@js.native
object ReactBootstrapTimePicker extends js.Object

@react object TimePicker extends ExternalComponent {

  case class Props(
    format: Option[Int] = None,
    initialValue: Option[Int] = None,
    onChange: Option[Int => Unit] = None,
    start: Option[String] = None,
    end: Option[String] = None,
    step: Option[Int] = None,
    value: Option[Int] = None
  )

  override val component = ReactBootstrapTimePicker
}