package com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.transformers

import com.harana.sdk.shared.models.flow.actionobjects.TransformerInfo
import com.harana.sdk.shared.models.flow.parameters.selections.{MultipleColumnSelection, NameColumnSelection}
import com.harana.sdk.shared.models.flow.parameters.{ColumnSelectorParameter, ParameterGroup, Parameters, SingleColumnCreatorParameter}

trait VectorAssemblerInfo extends TransformerInfo with Parameters {

  val id = "5EA37B3D-2D5C-4423-9631-FF3CF1177D0B"

  val inputColumnsParameter = ColumnSelectorParameter("input-columns", portIndex = 0)
  val outputColumnParameter = SingleColumnCreatorParameter("output-column")

  def setInputColumns(selection: Set[String]): this.type = set(inputColumnsParameter, MultipleColumnSelection(List(NameColumnSelection(selection))))
  def setOutputColumn(name: String): this.type = set(outputColumnParameter, name)

  override val parameterGroups = List(ParameterGroup("", inputColumnsParameter, outputColumnParameter))

}

object VectorAssemblerInfo extends VectorAssemblerInfo