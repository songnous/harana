package com.harana.ui.external.history

import org.scalajs.dom.{ History => DomHistory }

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport

@js.native
trait RichHistory extends DomHistory {
  def action: String                                = js.native
  def block(prompt: Boolean = false): Unit          = js.native
  def createHref(location: String): Unit            = js.native
  def goBack(): Unit                                = js.native
  def goForward(): Unit                             = js.native
  def listen(listener: js.Function0[Unit]): Unit    = js.native
  def location: String                              = js.native
  def push(route: String): Unit                     = js.native
  def replace(path: String, state: js.Object): Unit = js.native
}

@JSImport("history", JSImport.Default)
@js.native
object History extends js.Object {
  def createBrowserHistory(): RichHistory = js.native
}
