package com.harana.s3.services.cache

import com.harana.modules.core.config.Config
import com.harana.modules.core.logger.Logger
import com.harana.modules.core.micrometer.Micrometer
import com.harana.modules.vertx.models.Response
import io.vertx.core.http.HttpMethod
import io.vertx.ext.web.RoutingContext
import zio.clock.Clock
import zio.{Task, ZLayer}

import java.nio.ByteBuffer

object LiveCache {
  val layer = ZLayer.fromServices { (clock: Clock.Service,
                                     config: Config.Service,
                                     logger: Logger.Service,
                                     micrometer: Micrometer.Service) => new Cache.Service {
    def get(key: String): Task[ByteBuffer] =
      Task(null)

    def put(key: String, content: ByteBuffer): Task[Int] =
      Task(1)

    def remove(key: String): Task[Unit] =
      Task.unit

    def containsKey(key: String): Task[Boolean] =
      Task(false)
  }}
}
