package com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.estimators

import com.harana.sdk.shared.models.flow.actionobjects.EstimatorInfo
import com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.parameters.common.{HasFeaturesColumnParameter, HasLabelColumnParameter, HasOutputColumnParameter}
import com.harana.sdk.shared.models.flow.parameters.IntParameter
import com.harana.sdk.shared.models.flow.parameters.validators.RangeValidator

import scala.language.reflectiveCalls

trait UnivariateFeatureEstimatorInfo
  extends EstimatorInfo
    with HasFeaturesColumnParameter
    with HasOutputColumnParameter
    with HasLabelColumnParameter {

  val id = "509D82C5-76E1-4AB0-B119-154883DB0AB4"

  val numTopFeaturesParameter = IntParameter("num top features", validator = RangeValidator(begin = 1, end = Int.MaxValue, step = Some(1)))
  setDefault(numTopFeaturesParameter -> 50)
  def setNumTopFeatures(value: Int): this.type = set(numTopFeaturesParameter -> value)

  val parameters = Left(Array(
    numTopFeaturesParameter,
    featuresColumnParameter,
    outputColumnParameter,
    labelColumnParameter
  ))
}

object UnivariateFeatureEstimatorInfo extends UnivariateFeatureEstimatorInfo