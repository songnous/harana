package com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.estimators

import com.harana.sdk.shared.models.flow.actionobjects.EstimatorInfo
import com.harana.sdk.shared.models.flow.parameters.{BooleanParameter, Parameter, Parameters}

trait StandardScalerEstimatorInfo extends EstimatorInfo with Parameters {

  val id = "0772BF36-7EA7-4814-8B49-8BF1E9A67052"

  val withMeanParameter = BooleanParameter("with mean", Some("Whether to center data with mean."))
  setDefault(withMeanParameter, false)

  val withStdParameter = BooleanParameter("with std", Some("Whether to scale the data to unit standard deviation."))
  setDefault(withStdParameter, true)

  val parameters = Array[Parameter[_]](withMeanParameter, withStdParameter)
}

object StandardScalerEstimatorInfo extends StandardScalerEstimatorInfo