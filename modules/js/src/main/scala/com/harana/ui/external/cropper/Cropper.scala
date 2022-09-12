package com.harana.ui.external.cropper

import slinky.core.ExternalComponent
import slinky.core.annotations.react

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport

@JSImport("react-cropper", JSImport.Default)
@js.native
object ReactCropper extends js.Object {
  def on(eventname: String, callback: () => Unit): Unit = js.native
}

@react object Cropper extends ExternalComponent {
  type Props = Unit
  override val component = ReactCropper
}