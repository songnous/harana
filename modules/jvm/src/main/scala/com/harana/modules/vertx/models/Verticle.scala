package com.harana.modules.vertx.models

import io.vertx.core.{AbstractVerticle, Promise}
import zio._

trait Verticle extends AbstractVerticle {

  def run: ZIO[zio.ZEnv, Nothing, Int]

  override def start(startPromise: Promise[Void]): Unit = {
      Runtime.default.unsafeRun(
        (for {
          fiber <- run.fork
          _ <- IO.effectTotal(java.lang.Runtime.getRuntime.addShutdownHook(new Thread {
            override def run() = {
              val _ = Runtime.default.unsafeRunSync(fiber.interrupt)
            }
          }))
          result <- fiber.join
          _      <- fiber.interrupt
        } yield result).provideLayer(ZEnv.live)
      )
  }
}