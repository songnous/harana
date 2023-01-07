package com.harana.s3.services.server.models

trait PageSet[T] extends Set[T] {

  def nextMarker(): String

}
