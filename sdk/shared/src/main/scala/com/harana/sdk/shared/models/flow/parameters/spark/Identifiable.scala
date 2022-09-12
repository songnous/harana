package com.harana.sdk.shared.models.flow.parameters.spark

import java.util.UUID

trait Identifiable {
  val uid: String
  override def toString: String = uid
}

object Identifiable {
  def randomUID(prefix: String): String = {
    prefix + "_" + UUID.randomUUID().toString.takeRight(12)
  }
}