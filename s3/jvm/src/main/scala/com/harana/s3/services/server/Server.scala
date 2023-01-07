package com.harana.s3.services.server

import com.harana.modules.vertx.models.Response
import io.vertx.core.http.HttpMethod
import io.vertx.ext.web.RoutingContext
import zio.macros.accessible
import zio.{Has, Task}

@accessible
object Server {
  type Server = Has[Server.Service]

  trait Service {

    def handle(rc: RoutingContext, method: HttpMethod): Task[Response]

  }
}
