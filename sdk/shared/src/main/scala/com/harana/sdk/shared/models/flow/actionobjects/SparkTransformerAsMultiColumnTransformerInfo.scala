package com.harana.sdk.shared.models.flow.actionobjects

import com.harana.sdk.shared.models.flow.parameters.Parameter

import scala.language.reflectiveCalls

abstract class SparkTransformerAsMultiColumnTransformerInfo extends MultiColumnTransformerInfo {

  def convertInputNumericToVector: Boolean = false
  def convertOutputVectorToDouble: Boolean = false

  val specificParameters = Array.empty[Parameter[_]]

}