package com.harana.s3.services.server.models

import java.util.Objects.requireNonNull

case class S3Exception(error: S3ErrorCode,
                       message: String = "",
                       cause: Throwable = null,
                       elements: Map[String, String] = Map.empty) extends Exception(requireNonNull(message), cause)