package com.harana.modules.core.logger

import com.harana.modules.core.logger.Logger.Service
import org.apache.logging.log4j.{LogManager, Logger => Log4JLogger}
import org.apache.commons.io.FilenameUtils
import sourcecode._
import zio._

import java.util.concurrent.ConcurrentHashMap

object LiveLogger {
  val layer = ZLayer.succeed(new Service {

    private val loggers = new ConcurrentHashMap[String, Log4JLogger]()

    def error(message: => String)(implicit file: File): UIO[Unit] =
      UIO(logger(file).error(message))

    def warn(message: => String)(implicit file: File): UIO[Unit] =
      UIO(logger(file).warn(message))

    def info(message: => String)(implicit file: File): UIO[Unit] =
      UIO(logger(file).info(message))

    def debug(message: => String)(implicit file: File): UIO[Unit] =
      UIO(logger(file).debug(message))

    def trace(message: => String)(implicit file: File): UIO[Unit] =
      UIO(logger(file).trace(message))

    def error(t: Throwable)(message: => String)(implicit file: File): UIO[Unit] =
      UIO(logger(file).error(message, t))

    def warn(t: Throwable)(message: => String)(implicit file: File): UIO[Unit] =
      UIO(logger(file).warn(message, t))

    def info(t: Throwable)(message: => String)(implicit file: File): UIO[Unit] =
      UIO(logger(file).info(message, t))

    def debug(t: Throwable)(message: => String)(implicit file: File): UIO[Unit] =
      UIO(logger(file).debug(message, t))

    def trace(t: Throwable)(message: => String)(implicit file: File): UIO[Unit] =
      UIO(logger(file).trace(message, t))

    private def logger(file: File) =
      if (loggers.contains(file.value))
        loggers.get(file.value)
      else {
        val name = FilenameUtils.getBaseName(file.value)
        val logger = LogManager.getLogger(name)
        loggers.putIfAbsent(name, logger)
        logger
      }
  })
}