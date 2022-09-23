package com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.estimators

import com.harana.sdk.shared.models.flow.actionobjects.EstimatorInfo
import com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.parameters.common._

trait RandomForestRegressionInfo
  extends EstimatorInfo
    with PredictorParameters
    with HasLabelColumnParameter
    with HasSeedParameter
    with HasMaxDepthParameter
    with HasMinInstancePerNodeParameter
    with HasMaxBinsParameter
    with HasSubsamplingRateParameter
    with HasMinInfoGainParameter
    with HasMaxMemoryInMBParameter
    with HasCacheNodeIdsParameter
    with HasCheckpointIntervalParameter
    with HasNumTreesParameter
    with HasFeatureSubsetStrategyParameter
    with HasRegressionImpurityParameter {

  val id = "929760DE-008C-4C47-8648-55A001901B46"

  val parameters = Left(Array(
    maxDepthParameter,
    maxBinsParameter,
    minInstancesPerNodeParameter,
    minInfoGainParameter,
    maxMemoryInMBParameter,
    cacheNodeIdsParameter,
    checkpointIntervalParameter,
    impurityParameter,
    subsamplingRateParameter,
    seedParameter,
    numTreesParameter,
    featureSubsetStrategyParameter,
    labelColumnParameter,
    featuresColumnParameter,
    predictionColumnParameter
  ))
}

object RandomForestRegressionInfo extends RandomForestRegressionInfo