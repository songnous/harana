package com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.parameters.common

import com.harana.sdk.shared.models.flow.parameters
import com.harana.sdk.shared.models.flow.parameters.{IntParameter, Parameters}
import com.harana.sdk.shared.models.flow.parameters.validators.RangeValidator

import scala.language.reflectiveCalls

trait HasFeatureIndexParameter extends Parameters {

  val featureIndexParameter = IntParameter("feature index", Some("The index of the feature if features column is a vector column, no effect otherwise."),
    validator = RangeValidator.positiveIntegers
  )

  setDefault(featureIndexParameter, 0)
}