package com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.estimators

import com.harana.sdk.shared.models.flow.actionobjects.{ActionObjectInfo, EstimatorInfo}
import com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.parameters.common._
import com.harana.sdk.shared.models.flow.parameters.ParameterGroup

trait LogisticRegressionInfo
    extends ActionObjectInfo
      with EstimatorInfo
      with ProbabilisticClassifierParameters
      with HasLabelColumnParameter
      with HasThresholdParameter
      with HasRegularizationParameter
      with HasElasticNetParameter
      with HasMaxIterationsParameter
      with HasToleranceParameter
      with HasFitInterceptParameter
      with HasStandardizationParameter
      with HasOptionalWeightColumnParameter {

  val id = "875F842E-B130-45B8-80C3-55AD0B1317E7"

  override val maxIterationsDefault = 100

  override val parameterGroups = List(ParameterGroup("",
    elasticNetParameter,
    fitInterceptParameter,
    maxIterationsParameter,
    regularizationParameter,
    toleranceParameter,
    standardizationParameter,
    optionalWeightColumnParameter,
    labelColumnParameter,
    featuresColumnParameter,
    probabilityColumnParameter,
    rawPredictionColumnParameter,
    predictionColumnParameter,
    thresholdParameter
  ))
}

object LogisticRegressionInfo extends LogisticRegressionInfo
