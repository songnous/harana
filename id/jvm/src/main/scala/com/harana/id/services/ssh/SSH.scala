package com.harana.id.services.ssh

import com.harana.modules.vertx.models.Response
import io.vertx.ext.web.RoutingContext
import zio.macros.accessible
import zio.{Has, Task}

@accessible
object SSH {
  type SSH = Has[SSH.Service]

  trait Service {
    def authenticatePassword(rc: RoutingContext): Task[Response]

    def authenticatePublicKey(rc: RoutingContext): Task[Response]

    def configuration(rc: RoutingContext): Task[Response]
  }
}