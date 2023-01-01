package com.harana.s3.services.models

trait PageSet[T] extends Set[T] {

  def nextMarker(): String

}
