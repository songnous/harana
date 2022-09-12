package com.harana.sdk.shared.models.flow.actionobjects

import com.harana.sdk.shared.models.flow.parameters.{Parameter, StringParameter}

trait SqlColumnTransformerInfo extends MultiColumnTransformerInfo {

  override val id = "DCA2270C-D170-4AFC-B35B-DF9CD61397EB"

  val inputColumnAliasParameter = StringParameter("input column alias", Some("An identifier that can be used in SQL formula to refer the input column."))
  setDefault(inputColumnAliasParameter -> "x")
  def getInputColumnAlias = $(inputColumnAliasParameter)
  def setInputColumnAlias(value: String): this.type = set(inputColumnAliasParameter, value)

  val formulaParameter = StringParameter("formula", Some("SQL formula that uses input column as \"x\"."))
  setDefault(formulaParameter -> "x")
  def getFormula = $(formulaParameter)
  def setFormula(value: String): this.type = set(formulaParameter, value)

  val specificParameters = Array[Parameter[_]](inputColumnAliasParameter, formulaParameter)

}

object SqlColumnTransformerInfo extends SqlColumnTransformerInfo