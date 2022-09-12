package com.harana.designer.backend.services.help

import com.harana.modules.vertx.models.Response
import io.vertx.ext.web.RoutingContext
import zio.macros.accessible
import zio.{Has, Task}

@accessible
object Help {
   type Help = Has[Help.Service]

  trait Service {
    def list(rc: RoutingContext): Task[Response]

    def get(rc: RoutingContext): Task[Response]
  }
}