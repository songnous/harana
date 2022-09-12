package com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.parameters

import com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.parameters.common.{HasOptionalQuantilesColumnParameter, PredictorParameters}
import com.harana.sdk.shared.models.flow.parameters
import com.harana.sdk.shared.models.flow.parameters.{DoubleArrayParameter, Parameters}
import com.harana.sdk.shared.models.flow.parameters.validators.{ArrayLengthValidator, ComplexArrayValidator, RangeValidator}

import scala.language.reflectiveCalls

trait AFTSurvivalRegressionParameters
  extends Parameters
    with PredictorParameters
    with HasOptionalQuantilesColumnParameter {

  val quantileProbabilitiesParameter = DoubleArrayParameter(
    name = "quantile probabilities",
    description = Some("""Param for quantile probabilities array.
                         |Values of the quantile probabilities array should be in the range (0, 1)
                         |and the array should be non-empty.""".stripMargin),
    validator = ComplexArrayValidator(
      rangeValidator = RangeValidator(0.0, 1.0, beginIncluded = false, endIncluded = false),
      lengthValidator = ArrayLengthValidator.withAtLeast(1)
    )
  )

  setDefault(quantileProbabilitiesParameter, Array(0.01, 0.05, 0.1, 0.25, 0.5, 0.75, 0.9, 0.95, 0.99))
}