package com.harana.ui.external.diagrams

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport

trait DefaultLabelModelOptions extends LabelModelOptions {
  val label: String
}

trait DefaultLabelModelGenerics extends LabelModelGenerics {
  override val OPTIONS: DefaultLabelModelOptions
}

@js.native
@JSImport("@projectstorm/react-diagrams-defaults", "DefaultLabelModel")
class DefaultLabelModel[G <: DefaultLabelModelGenerics, L <: BaseModelListener](options: DefaultLabelModelOptions) extends LabelModel[G, L](options) {
  def setLabel(label: String): Unit = js.native
  override def serialize(): js.Object = js.native
  override def deserialize(event: js.Object): Unit = js.native
}

trait LabelModelOptions extends BaseModelOptions {
  val offsetX: Int
  val offsetY: Int
}

trait LabelModelGenerics extends BaseModelGenerics {
  override val OPTIONS: LabelModelOptions
  override val PARENT: LinkModel[_,_]
}

@js.native
@JSImport("@projectstorm/react-diagrams-core", "LabelModel")
class LabelModel[G <: LabelModelGenerics, L <: BaseModelListener](options: LabelModelOptions) extends BaseModel[G, L](options) {
  override def serialize(): js.Object = js.native
  override def deserialize(event: js.Object): Unit = js.native
}