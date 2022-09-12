package com.harana.utils.bson.codec

case class CodecException(msg: String, cause: Option[Throwable] = None) extends RuntimeException(msg) {
  cause.foreach(initCause)
}