package com.harana.s3.services.server.models

import zio.{IO, Task}

import java.util.Objects.requireNonNull

case class S3Exception(error: S3ErrorCode,
                       message: String = "",
                       cause: Throwable = null,
                       elements: Map[String, String] = Map.empty) extends Exception(requireNonNull(message), cause)

object S3Exception {

  implicit def fromTask[T](task: Task[T]): IO[S3Exception, T] =
      task.mapError(e => S3Exception(S3ErrorCode.UNKNOWN_ERROR, e.getMessage, e.fillInStackTrace()))

}