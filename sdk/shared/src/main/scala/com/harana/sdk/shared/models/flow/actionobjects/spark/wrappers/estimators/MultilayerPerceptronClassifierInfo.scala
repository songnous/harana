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

  val layersParameter = IntArrayParameter(
    name = "layers",
    description = Some("""The list of layer sizes that includes the input layer size as the first number and the
                         |output layer size as the last number. The input layer and hidden layers have sigmoid
                         |activation functions, while the output layer has a softmax. The input layer size has to be
                         |equal to the length of the feature vector. The output layer size has to be equal to the
                         |total number of labels.""".stripMargin),
    validator = ComplexArrayValidator(RangeValidator.positiveIntegers, ArrayLengthValidator.withAtLeast(2))
  )

  setDefault(layersParameter, Array(1, 1))

  val parameters = Array(
    layersParameter,
    maxIterationsParameter,
    seedParameter,
    toleranceParameter,
    labelColumnParameter,
    featuresColumnParameter,
    predictionColumnParameter
  )
}

object MultilayerPerceptronClassifierInfo extends MultilayerPerceptronClassifierInfo