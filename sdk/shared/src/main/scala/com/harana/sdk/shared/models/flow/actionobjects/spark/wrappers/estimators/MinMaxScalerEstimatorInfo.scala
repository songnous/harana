package com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.estimators

import com.harana.sdk.shared.models.flow.actionobjects.EstimatorInfo
import com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.parameters.common.{HasInputColumnParameter, HasOutputColumnParameter, MinMaxParameters}
import com.harana.sdk.shared.models.flow.parameters.Parameter

trait MinMaxScalerEstimatorInfo
  extends EstimatorInfo
    with MinMaxParameters
    with HasInputColumnParameter
    with HasOutputColumnParameter {

  val id = "F73D87C3-8773-498A-B7A2-C1E127BE1067"

  val specificParameters = Array[Parameter[_]](minParameter, maxParameter)
}

object MinMaxScalerEstimatorInfo extends MinMaxScalerEstimatorInfo {
  val parameters = Left(Array.empty[Parameter[_]])
}
