package com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.transformers

import com.harana.sdk.shared.models.flow.actionobjects.TransformerInfo
import com.harana.sdk.shared.models.flow.parameters.{BooleanParameter, Parameter, ParameterGroup, Parameters}

trait DiscreteCosineTransformerInfo extends TransformerInfo with Parameters {

  val id = "665D19BA-8817-4268-8CB1-7B7572E95A8B"

  val inverseParameter = BooleanParameter("inverse", default = Some(false))
  def getInverse = $(inverseParameter)

  val specificParameters = Array[Parameter[_]](inverseParameter)

}

object DiscreteCosineTransformerInfo extends DiscreteCosineTransformerInfo {
  override val parameterGroups = List.empty[ParameterGroup]
}