package com.harana.sdk.shared.models.flow.actionobjects

import SortColumnParameter.{columnNameParameterName, descendingFlagParameterName}
import com.harana.sdk.shared.models.flow.parameters.selections.{IndexSingleColumnSelection, NameSingleColumnSelection, SingleColumnSelection}
import com.harana.sdk.shared.models.flow.parameters.{BooleanParameter, Parameters, ParametersSequence, SingleColumnSelectorParameter}

trait SortTransformerInfo extends TransformerInfo {

  val id = "C40584ED-56CB-4A8C-92E8-56105C76FF92"

  val columnsParameter = ParametersSequence[SortColumnParameter]("sort columns")
  def getColumns = $(columnsParameter)
  def setColumns(sortColumnParameters: Seq[SortColumnParameter]): this.type = set(columnsParameter, sortColumnParameters)
  val parameters = Left(List(columnsParameter))

}

object SortTransformerInfo extends SortTransformerInfo

class SortColumnParameter extends Parameters {

  val columnNameParameter = SingleColumnSelectorParameter(columnNameParameterName, portIndex = 0)
  def getColumnName = $(columnNameParameter)
  def setColumnName(col: SingleColumnSelection): this.type = set(columnNameParameter, col)

  val descendingParameter = BooleanParameter(descendingFlagParameterName, default = Some(false))
  def getDescending = $(descendingParameter)
  def isDescending: Boolean = getDescending
  def setDescending(desc: Boolean): this.type = set(descendingParameter, desc)

  val parameters = Left(List(columnNameParameter, descendingParameter))
}

object SortColumnParameter {

  val columnNameParameterName = "column name"
  val descendingFlagParameterName = "descending"

  def apply(columnName: String, descending: Boolean) =
    new SortColumnParameter().setColumnName(new NameSingleColumnSelection(columnName)).setDescending(descending)

  def apply(columnIndex: Int, descending: Boolean) =
    new SortColumnParameter().setColumnName(new IndexSingleColumnSelection(columnIndex)).setDescending(descending)

  def apply(columnSelection: SingleColumnSelection, descending: Boolean) =
    new SortColumnParameter().setColumnName(columnSelection).setDescending(descending)

}