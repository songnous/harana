package com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.estimators

import com.harana.sdk.shared.models.flow.actionobjects.EstimatorInfo
import com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.parameters.LinearRegressionParameters

trait LinearRegressionInfo extends EstimatorInfo with LinearRegressionParameters {

  val id = "13B22557-FD3E-4C05-9F53-EACEE20297DD"

  val parameters = Left(List(
    elasticNetParameter,
    fitInterceptParameter,
    maxIterationsParameter,
    regularizationParameter,
    toleranceParameter,
    standardizationParameter,
    optionalWeightColumnParameter,
    solverParameter,
    labelColumnParameter,
    featuresColumnParameter,
    predictionColumnParameter
  ))

}

object LinearRegressionInfo extends LinearRegressionInfo