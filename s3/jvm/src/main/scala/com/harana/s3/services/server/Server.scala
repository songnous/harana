package com.harana.s3.services.server

import com.harana.modules.vertx.models.Response
import io.vertx.core.http.HttpMethod
import io.vertx.ext.web.RoutingContext
import zio.macros.accessible
import zio.{Has, Task}

@accessible
object S3 {
  type S3 = Has[S3.Service]

  trait Service {

    def handle(rc: RoutingContext, method: HttpMethod): Task[Response]

  }
}
