package com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.parameters.common

import com.harana.sdk.shared.models.flow.parameters.validators.RangeValidator
import com.harana.sdk.shared.models.flow.parameters.{IntParameter, Parameters}

import scala.language.reflectiveCalls

trait HasFeatureIndexParameter extends Parameters {

  val featureIndexParameter = IntParameter("feature-index", default = Some(0), validator = RangeValidator.positiveIntegers)

}