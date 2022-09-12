package com.harana.modules.sentry

import io.sentry.Breadcrumb
import zio.{Has, UIO}
import zio.macros.accessible

@accessible
object Sentry {
  type Sentry = Has[Sentry.Service]

  trait Service {

    def addBreadcrumb(message: String): UIO[Unit]
    def addBreadcrumb(breadcrumb: Breadcrumb): UIO[Unit]

  }
}