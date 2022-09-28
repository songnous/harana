package com.harana.sdk.shared.models.flow.actions

trait UIActionInfo[T] {
  def apply(pos: (Int, Int), color: Option[String] = None): T
}
