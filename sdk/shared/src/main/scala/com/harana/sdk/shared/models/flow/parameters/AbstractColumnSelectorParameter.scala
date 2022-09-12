package com.harana.sdk.shared.models.flow.parameters

abstract class AbstractColumnSelectorParameter[T] extends Parameter[T] {
  val isSingle: Boolean
  val portIndex: Int
}