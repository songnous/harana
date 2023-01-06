package com.harana.s3.services.cache

import com.harana.modules.core.config.Config
import com.harana.modules.core.logger.Logger
import com.harana.modules.core.micrometer.Micrometer
import com.harana.modules.vertx.models.Response
import io.vertx.core.http.HttpMethod
import io.vertx.ext.web.RoutingContext
import zio.clock.Clock
import zio.{Task, ZLayer}

object LiveCache {
  val layer = ZLayer.fromServices { (clock: Clock.Service,
                                     config: Config.Service,
                                     logger: Logger.Service,
                                     micrometer: Micrometer.Service) => new Cache.Service {

      def handle(rc: RoutingContext, method: HttpMethod): Task[Response] =
        for {
          _ <- logger.info(rc.request().toString)
          _ = rc.response().headers().add(AwsHttpHeaders.REQUEST_ID.value, "4442587FB7D0A2F9")


        } yield ()

    })
  }
}
