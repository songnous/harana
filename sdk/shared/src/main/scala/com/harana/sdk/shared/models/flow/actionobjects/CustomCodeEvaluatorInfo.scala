package com.harana.sdk.shared.models.flow.actionobjects

import com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.parameters.common.HasIsLargerBetterParameter
import com.harana.sdk.shared.models.flow.parameters.{CodeSnippetParameter, StringParameter}

trait CustomCodeEvaluatorInfo extends EvaluatorInfo with HasIsLargerBetterParameter {

  val InputPortNumber: Int = 0
  val OutputPortNumber: Int = 0

  val metricNameParameter = StringParameter("metric name", default = Some("custom metric"))
  def getMetricName = $(metricNameParameter)

  val codeParameter: CodeSnippetParameter

  def isLargerBetter = $(isLargerBetterParameter)

  lazy val parameters = Left(Array(metricNameParameter, codeParameter, isLargerBetterParameter))
  
}