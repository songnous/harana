package com.harana.designer.backend.services.terminal

import com.harana.modules.vertx.models.Response
import io.vertx.ext.web.RoutingContext
import zio.macros.accessible
import zio.{Has, Task}

@accessible
object Terminal {
   type Terminal = Has[Terminal.Service]

  trait Service {

    def startTerminal(rc: RoutingContext): Task[Response]

    def stopTerminal(rc: RoutingContext): Task[Response]

    def isTerminalRunning(rc: RoutingContext): Task[Response]

    def configuration(rc: RoutingContext): Task[Response]
  }
}