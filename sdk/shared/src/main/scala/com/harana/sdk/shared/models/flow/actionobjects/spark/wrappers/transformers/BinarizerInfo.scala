package com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.transformers

import com.harana.sdk.shared.models.flow.actionobjects.TransformerInfo
import com.harana.sdk.shared.models.flow.parameters.{DoubleParameter, Parameter, Parameters}

trait BinarizerInfo extends TransformerInfo with Parameters {

  val id = "837220CE-60D9-4D7E-925D-81AFCD82AFD1"

  val thresholdParameter = DoubleParameter(
    name = "threshold",
    description = Some("""The threshold used to binarize continuous features. Feature values greater
                         |than the threshold will be binarized to 1.0. Remaining values will be binarized
                         |to 0.0.""".stripMargin),
  )

  setDefault(thresholdParameter, 0.0)

  val specificParameters = Array[Parameter[_]](thresholdParameter)

  def setThreshold(value: Double): this.type = set(thresholdParameter, value)

}

object BinarizerInfo extends BinarizerInfo {
  val parameters = Array.empty
}