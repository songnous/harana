package com.harana.modules

import zio.{IO, UIO, ZIO}

import scala.concurrent.Future

package object stripe {

  def execute[E, A](output: Future[Either[E, A]]): IO[E, A] =
    UIO(output).flatMap { o =>
      ZIO.fromFuture { _ =>
        o
      }.orDie.absolve
    }
}