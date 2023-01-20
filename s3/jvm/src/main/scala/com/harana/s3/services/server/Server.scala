package com.harana.s3.services.server

import com.harana.modules.vertx.models.Response
import com.harana.s3.services.server.models.S3Exception
import io.vertx.core.http.HttpMethod
import io.vertx.ext.web.RoutingContext
import zio.macros.accessible
import zio.{Has, IO, Task}

@accessible
object Server {
  type Server = Has[Server.Service]

  trait Service {

    def handle(rc: RoutingContext): Task[Response]

  }
}
