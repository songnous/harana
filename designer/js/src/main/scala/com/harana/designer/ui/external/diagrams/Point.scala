package com.harana.ui.external.diagrams

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport

trait PointModelOptions extends BasePositionModelOptions {
  val link: LinkModel[_,_]
}

trait PointModelGenerics extends BasePositionModelGenerics {
  override val OPTIONS: PointModelOptions
  override val PARENT: LinkModel[_,_]
}

@js.native
@JSImport("@projectstorm/react-diagrams-core", "PointModel")
class PointModel[G <: PointModelGenerics, L <: BasePositionModelListener](options: PointModelOptions) extends BasePositionModel[G, L](options) {
  def getLink: LinkModel[_,_] = js.native
  def isConnectedToPort(): Boolean = js.native
  override def isLocked(): Boolean = js.native
  override def remove(): Unit = js.native
}