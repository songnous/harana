package com.harana.designer.electron

import scala.scalajs.js
import js.annotation.{JSGlobal, JSName}

/**
 * See http://electron.atom.io/docs/v0.30.0/api/process/
 */
@JSGlobal
@js.native
object Process extends js.Object {

  def platform: String = js.native

}
