package com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.estimators

import com.harana.sdk.shared.models.flow.ActionObjectInfo
import com.harana.sdk.shared.models.flow.actionobjects.EstimatorInfo
import com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.parameters.common._

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

  val parameters = Left(List(
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
