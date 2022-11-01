package com.harana.designer.backend.services

import org.apache.logging.log4j.LogManager
import zio.{Runtime, Task}
import zio.internal.Platform

package object terminals {

  val logger = LogManager.getLogger("Vertx")

  val runtime = Runtime[Unit]((), Platform.default
    .withReportFailure(cause => if (!cause.interrupted) logger.error(cause.prettyPrint)))

  @inline
  def run[A](zio: Task[A]): A = runtime.unsafeRun(zio)

  @inline
  def runAsync(zio: Task[_]): Unit = runtime.unsafeRunAsync_(zio)

}
