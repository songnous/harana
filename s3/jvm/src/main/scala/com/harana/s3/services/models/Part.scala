package com.harana.s3.services.models

case class Part(number: String, lastModified: Option[Long], eTag: Option[String], size: Long)
