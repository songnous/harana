package com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.estimators

import com.harana.sdk.shared.models.flow.actionobjects.EstimatorInfo
import com.harana.sdk.shared.models.flow.parameters
import com.harana.sdk.shared.models.flow.parameters.{IntParameter, Parameter, Parameters}
import com.harana.sdk.shared.models.flow.parameters.validators.RangeValidator

trait PCAEstimatorInfo extends EstimatorInfo with Parameters {

  val id = "D0CF1ACF-247F-4CEE-9E8F-6D22B0487646"

  val kParameter = IntParameter("k", validator = RangeValidator(begin = 1, end = Int.MaxValue, step = Some(1)))
  setDefault(kParameter, 1)
  def setK(value: Int): this.type = set(kParameter -> value)

  val specificParameters = Array[Parameter[_]](kParameter)

}

object PCAEstimatorInfo extends PCAEstimatorInfo {
  val parameters = Array.empty[Parameter[_]]
}