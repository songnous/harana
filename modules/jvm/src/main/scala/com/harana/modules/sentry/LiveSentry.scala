package com.harana.modules.sentry

import com.harana.modules.core.config.Config
import com.harana.modules.core.logger.Logger
import com.harana.modules.core.micrometer.Micrometer
import com.harana.modules.sentry.Sentry.Service
import io.sentry.{Breadcrumb, Sentry => ioSentry}
import zio.{UIO, ZLayer}

object LiveSentry {
  val layer = ZLayer.fromServices { (config: Config.Service,
                                   logger: Logger.Service,
                                   micrometer: Micrometer.Service) => new Service {

      config.string("sentry.dsn", "").map(ioSentry.init)

    def addBreadcrumb(message: String): UIO[Unit] = {
      UIO(ioSentry.addBreadcrumb(message))
    }

    def addBreadcrumb(breadcrumb: Breadcrumb): UIO[Unit] = {
      UIO(ioSentry.addBreadcrumb(breadcrumb))
    }

  }}
}