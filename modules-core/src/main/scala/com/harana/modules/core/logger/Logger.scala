package com.harana.modules.core.logger

import sourcecode._
import zio.macros.accessible
import zio.{Has, UIO}

@accessible
object Logger {
  type Logger = Has[Logger.Service]

  trait Service {
    def error(message: => String)(implicit fullName: FullName): UIO[Unit]

    def warn(message: => String)(implicit fullName: FullName): UIO[Unit]

    def info(message: => String)(implicit fullName: FullName): UIO[Unit]

    def debug(message: => String)(implicit fullName: FullName): UIO[Unit]

    def trace(message: => String)(implicit fullName: FullName): UIO[Unit]

    def error(t: Throwable)(message: => String)(implicit fullName: FullName): UIO[Unit]

    def warn(t: Throwable)(message: => String)(implicit fullName: FullName): UIO[Unit]

    def info(t: Throwable)(message: => String)(implicit fullName: FullName): UIO[Unit]

    def debug(t: Throwable)(message: => String)(implicit fullName: FullName): UIO[Unit]

    def trace(t: Throwable)(message: => String)(implicit fullName: FullName): UIO[Unit]
  }
}
