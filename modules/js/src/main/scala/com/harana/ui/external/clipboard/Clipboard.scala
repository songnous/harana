package com.harana.ui.external.clipboard

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport

@JSImport("clipboard", JSImport.Default)
@js.native
class Clipboard(selector: String) extends js.Object {
  def destroy(): Unit = js.native
}
