package com.harana.designer.frontend

import com.harana.designer.frontend.analytics.Analytics
import typings.std
import typings.vertx3EventbusClient.mod.{^ => VertxEventBus}

import scala.scalajs.js
import scala.scalajs.js.Dynamic.global

object EventBus {

  type Address = String

  def unsubscribe(address: Address) =
    eventBus.unregisterHandler(address)

  val eventBus: VertxEventBus = new VertxEventBus("/eventbus") {
    override def onopen() = {
      //println("Connecting to EventBus ..")
      enablePing(false)
      enableReconnect(true)

      eventBus.send("connectx", Main.claims.userId, new EventBusHeaders {
        val `type` = "connection"
        val group = "connection"
      })

      eventBus.registerHandler("connect2", js.undefined, (error: std.Error, result: js.Any) => {
        println("CONNECT2 MESSAGE")
      })

      eventBus.registerHandler(Main.claims.userId, js.undefined, (error: std.Error, result: js.Any) => {
        val message = result.asInstanceOf[EventBusMessage]
        global.console.dir(message)
      })
    }
    override def onerror(error: std.Error) = global.console.dir(error)
  }
}

trait EventBusMessage extends js.Object {
  val address: String
  val body: String
  val headers: EventBusHeaders
}

trait EventBusHeaders extends js.Object {
  val group: String
  val `type`: String
}
