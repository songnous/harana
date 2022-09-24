package com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.estimators

import com.harana.sdk.shared.models.flow.actionobjects.EstimatorInfo
import com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.parameters.common._
import com.harana.sdk.shared.models.flow.parameters.IntArrayParameter
import com.harana.sdk.shared.models.flow.parameters.validators.{ArrayLengthValidator, ComplexArrayValidator, RangeValidator}

trait MultilayerPerceptronClassifierInfo
    extends EstimatorInfo
    with PredictorParameters
    with HasLabelColumnParameter
    with HasMaxIterationsParameter
    with HasSeedParameter
    with HasToleranceParameter {

  val id = "39A5C3EE-6583-424C-BB08-54248C570C5E"

  override val maxIterationsDefault = 100
  override val toleranceDefault = 1e-4

  val layersParameter = IntArrayParameter("layers", default = Some(Array(1, 1)), validator = ComplexArrayValidator(RangeValidator.positiveIntegers, ArrayLengthValidator.withAtLeast(2)))

  val parameters = Left(Array(
    layersParameter,
    maxIterationsParameter,
    seedParameter,
    toleranceParameter,
    labelColumnParameter,
    featuresColumnParameter,
    predictionColumnParameter
  ))
}

object MultilayerPerceptronClassifierInfo extends MultilayerPerceptronClassifierInfo