package com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.transformers

import com.harana.sdk.shared.models.flow.actionobjects.TransformerInfo
import com.harana.sdk.shared.models.flow.parameters.{DoubleParameter, Parameter, ParameterGroup, Parameters}

trait BinarizerInfo extends TransformerInfo with Parameters {

  val id = "837220CE-60D9-4D7E-925D-81AFCD82AFD1"

  val thresholdParameter = DoubleParameter("threshold", default = Some(0.0))

  val specificParameters = Array[Parameter[_]](thresholdParameter)

  def setThreshold(value: Double): this.type = set(thresholdParameter, value)

}

object BinarizerInfo extends BinarizerInfo {
  override val parameterGroups = List.empty[ParameterGroup]
}