package com.harana.designer.backend.services.system

import com.harana.modules.vertx.models.Response
import io.vertx.ext.web.RoutingContext
import zio.macros.accessible
import zio.{Has, Task, UIO}

@accessible
object System {
  type System = Has[System.Service]

  trait Service {

    def createIndexes: Task[Unit]

    def content(rc: RoutingContext): Task[Response]

    def error(rc: RoutingContext): Task[Response]

    def i18n(locale: String): Task[String]

    def events: Task[Unit]

    def health(rc: RoutingContext): Task[Response]
  }
}