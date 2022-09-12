package com.harana.designer.backend.services.support

import com.harana.designer.backend.services.support.Support.Service
import com.harana.modules.core.config.Config
import com.harana.modules.core.logger.Logger
import com.harana.modules.core.micrometer.Micrometer
import com.harana.modules.vertx.models.Response
import io.vertx.ext.web.RoutingContext
import zio.{Task, ZLayer}

object LiveSupport {
  val layer = ZLayer.fromServices { (config: Config.Service,
                                     logger: Logger.Service,
                                     micrometer: Micrometer.Service) => new Service {

    def list(rc: RoutingContext): Task[Response] = {
      null
    }

    def search(rc: RoutingContext): Task[Response] = {
      null
    }

    def get(rc: RoutingContext): Task[Response] = {
      null
    }

    def delete(rc: RoutingContext): Task[Response] = {
      null
    }

    def create(rc: RoutingContext): Task[Response] = {
      null
    }

    def update(rc: RoutingContext): Task[Response] = {
      null
    }

    def close(rc: RoutingContext): Task[Response] = {
      null
    }

  }}
}