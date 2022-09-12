package com.harana.modules

import java.util.Optional
import java.util.concurrent.CompletableFuture

import com.hubspot.algebra.Result
import com.hubspot.slack.client.models.response.SlackError
import zio.{IO, ZIO}

import scala.collection.JavaConverters._
import scala.compat.java8.FutureConverters._
import scala.compat.java8.OptionConverters._
import scala.concurrent.ExecutionContext.Implicits.global

package object slack {

  implicit def toIO[SlackError, A](fn: CompletableFuture[Result[A, SlackError]]): IO[Either[SlackError, Throwable], A] =
    IO.effectAsync { cb =>
      fn.toScala.onComplete { f =>
        f.toEither match {
          case Left(t) => cb(IO.fail(Right(t)))
          case Right(x) => try {
            if (x.isOk) cb(IO.succeed(x.unwrapOrElseThrow()))
            else cb(IO.fail(Left(x.unwrapErrOrElseThrow())))
          } catch {
            case e: Exception => cb(IO.fail(Right(e)))
          }
        }
      }
    }

  implicit def toIOIterable[A](fn: java.lang.Iterable[CompletableFuture[Result[java.util.List[A], SlackError]]]): IO[Either[SlackError, Throwable], List[A]] =
    ZIO.foreach(fn.asScala.toList)(toIO).map(_.flatMap(_.asScala.toList))

  implicit def toOptionalInt(opt: Option[Int]): Optional[Integer] =
    opt.map { o => new Integer(o) }.asJava

  implicit def toOptionalDefault[A](opt: Option[A]): Optional[A] =
    opt.asJava
}
