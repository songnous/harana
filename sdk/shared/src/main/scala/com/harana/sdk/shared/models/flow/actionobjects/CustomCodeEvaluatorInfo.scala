package com.harana.sdk.shared.models.flow.actionobjects

import com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.parameters.common.HasIsLargerBetterParameter
import com.harana.sdk.shared.models.flow.parameters.{CodeSnippetParameter, StringParameter}

trait CustomCodeEvaluatorInfo extends EvaluatorInfo with HasIsLargerBetterParameter {

  val InputPortNumber: Int = 0
  val OutputPortNumber: Int = 0

  val metricNameParameter = StringParameter(name = "metric name")
  setDefault(metricNameParameter -> "custom metric")
  def getMetricName = $(metricNameParameter)

  val codeParameter: CodeSnippetParameter

  lazy val parameters = Array(metricNameParameter, codeParameter, isLargerBetterParameter)

  def isLargerBetter = $(isLargerBetterParameter)

}