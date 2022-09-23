package com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.estimators

import com.harana.sdk.shared.models.flow.actionobjects.EstimatorInfo
import com.harana.sdk.shared.models.flow.parameters
import com.harana.sdk.shared.models.flow.parameters.{IntParameter, Parameter, Parameters}
import com.harana.sdk.shared.models.flow.parameters.validators.RangeValidator

import scala.language.reflectiveCalls

trait QuantileDiscretizerEstimatorInfo extends EstimatorInfo with Parameters {

  val id = "BE138D50-B5A8-428B-8600-1F22718A5BCC"

  val numBucketsParameter = IntParameter("num buckets", RangeValidator(2, Int.MaxValue, step = Some(1)))
  setDefault(numBucketsParameter, 2)
  val specificParameters = Array[Parameter[_]](numBucketsParameter)
  def setNumBuckets(value: Int): this.type = set(numBucketsParameter -> value)

}

object QuantileDiscretizerEstimatorInfo extends QuantileDiscretizerEstimatorInfo {
  val parameters = Left(Array.empty[Parameter[_]])
}