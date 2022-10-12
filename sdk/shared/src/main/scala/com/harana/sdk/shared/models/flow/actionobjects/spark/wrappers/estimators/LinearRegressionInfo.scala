package com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.estimators

import com.harana.sdk.shared.models.flow.actionobjects.EstimatorInfo
import com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.parameters.LinearRegressionParameters
import com.harana.sdk.shared.models.flow.parameters.ParameterGroup

trait LinearRegressionInfo extends EstimatorInfo with LinearRegressionParameters {

  val id = "13B22557-FD3E-4C05-9F53-EACEE20297DD"

  override val parameterGroups = List(ParameterGroup("",
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