package typings.std.global

import typings.std.AddEventListenerOptions
import typings.std.Event
import typings.std.Window
import scala.scalajs.js
import scala.scalajs.js.`|`
import scala.scalajs.js.annotation._

@JSGlobal("addEventListener")
@js.native
object addEventListener_scroll extends js.Object {
  def apply(
    `type`: typings.std.stdStrings.scroll,
    listener: js.ThisFunction1[/* this */ Window, /* ev */ Event, _]
  ): Unit = js.native
  def apply(
    `type`: typings.std.stdStrings.scroll,
    listener: js.ThisFunction1[/* this */ Window, /* ev */ Event, _],
    options: scala.Boolean
  ): Unit = js.native
  def apply(
    `type`: typings.std.stdStrings.scroll,
    listener: js.ThisFunction1[/* this */ Window, /* ev */ Event, _],
    options: AddEventListenerOptions
  ): Unit = js.native
}

