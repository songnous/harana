package com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.estimators

import com.harana.sdk.shared.models.flow.actionobjects.EstimatorInfo
import com.harana.sdk.shared.models.flow.parameters.{BooleanParameter, Parameter, ParameterGroup, Parameters}

trait StandardScalerEstimatorInfo extends EstimatorInfo with Parameters {

  val id = "0772BF36-7EA7-4814-8B49-8BF1E9A67052"

  val withMeanParameter = BooleanParameter("with-mean", default = Some(false))
  val withStdParameter = BooleanParameter("with-std", default = Some(true))

  val specificParameters = Array[Parameter[_]](withMeanParameter, withStdParameter)

}

object StandardScalerEstimatorInfo extends StandardScalerEstimatorInfo {
  override val parameterGroups = List(ParameterGroup("", specificParameters.toList: _*))
}