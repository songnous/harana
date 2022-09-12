package typings.std.global

import typings.std.AddEventListenerOptions
import typings.std.Window
import typings.std.stdStrings.messageerror
import scala.scalajs.js
import scala.scalajs.js.`|`
import scala.scalajs.js.annotation._

@JSGlobal("addEventListener")
@js.native
object addEventListener_messageerror extends js.Object {
  def apply(
    `type`: messageerror,
    listener: js.ThisFunction1[/* this */ Window, /* ev */ typings.std.MessageEvent, _]
  ): Unit = js.native
  def apply(
    `type`: messageerror,
    listener: js.ThisFunction1[/* this */ Window, /* ev */ typings.std.MessageEvent, _],
    options: scala.Boolean
  ): Unit = js.native
  def apply(
    `type`: messageerror,
    listener: js.ThisFunction1[/* this */ Window, /* ev */ typings.std.MessageEvent, _],
    options: AddEventListenerOptions
  ): Unit = js.native
}

