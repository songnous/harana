package com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.estimators

import com.harana.sdk.shared.models.flow.actionobjects.EstimatorInfo
import com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.estimators.StandardScalerEstimatorInfo.specificParameters
import com.harana.sdk.shared.models.flow.parameters.{BooleanParameter, Parameter, Parameters}

trait StandardScalerEstimatorInfo extends EstimatorInfo with Parameters {

  val id = "0772BF36-7EA7-4814-8B49-8BF1E9A67052"

  val withMeanParameter = BooleanParameter("with mean")
  setDefault(withMeanParameter, false)

  val withStdParameter = BooleanParameter("with std")
  setDefault(withStdParameter, true)

  val specificParameters = Array[Parameter[_]](withMeanParameter, withStdParameter)
  val parameters = Left(specificParameters)

}

object StandardScalerEstimatorInfo extends StandardScalerEstimatorInfo