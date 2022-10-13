package com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.estimators

import com.harana.sdk.shared.models.flow.actionobjects.EstimatorInfo
import com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.parameters.common.{HasFeaturesColumnParameter, HasLabelColumnParameter, HasOutputColumnParameter}
import com.harana.sdk.shared.models.flow.parameters.validators.RangeValidator
import com.harana.sdk.shared.models.flow.parameters.{IntParameter, ParameterGroup}

import scala.language.reflectiveCalls

trait UnivariateFeatureSelectorEstimatorInfo
  extends EstimatorInfo
    with HasFeaturesColumnParameter
    with HasOutputColumnParameter
    with HasLabelColumnParameter {

  val id = "509D82C5-76E1-4AB0-B119-154883DB0AB4"

  val numTopFeaturesParameter = IntParameter("num-top-features", default = Some(50), validator = RangeValidator(begin = 1, end = Int.MaxValue, step = Some(1)))
  def setNumTopFeatures(value: Int): this.type = set(numTopFeaturesParameter -> value)

  override val parameterGroups = List(ParameterGroup("",
    numTopFeaturesParameter,
    featuresColumnParameter,
    outputColumnParameter,
    labelColumnParameter
  ))
}