package com.harana.s3.services.s3_server.models

trait PageSet[T] extends Set[T] {

  def nextMarker(): String

}
