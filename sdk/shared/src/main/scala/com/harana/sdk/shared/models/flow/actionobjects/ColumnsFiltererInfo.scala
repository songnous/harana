package com.harana.sdk.shared.models.flow.actionobjects

import com.harana.sdk.shared.models.flow.parameters.ColumnSelectorParameter
import com.harana.sdk.shared.models.flow.parameters.selections.{MultipleColumnSelection, NameColumnSelection}

trait ColumnsFiltererInfo extends TransformerInfo {

  val id = "63C65F84-4B13-4D35-BC67-17F5AA86EB76"

  val selectedColumnsParameter = ColumnSelectorParameter("selected columns", Some("Columns to be retained in the output DataFrame."), portIndex = 0)
  def getSelectedColumns = $(selectedColumnsParameter)
  def setSelectedColumns(value: MultipleColumnSelection): this.type = set(selectedColumnsParameter, value)
  def setSelectedColumns(retainedColumns: Seq[String]): this.type = setSelectedColumns(MultipleColumnSelection(Vector(NameColumnSelection(retainedColumns.toSet))))

  override val parameters = Array(selectedColumnsParameter)

}

object ColumnsFiltererInfo extends ColumnsFiltererInfo