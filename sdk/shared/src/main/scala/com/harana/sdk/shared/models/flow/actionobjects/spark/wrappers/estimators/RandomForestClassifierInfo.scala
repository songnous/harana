package com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.estimators

import com.harana.sdk.shared.models.flow.actionobjects.EstimatorInfo
import com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.parameters.common._
import com.harana.sdk.shared.models.flow.parameters.ParameterGroup

trait RandomForestClassifierInfo
  extends EstimatorInfo
    with HasMaxDepthParameter
    with HasMaxBinsParameter
    with HasMinInstancePerNodeParameter
    with HasMinInfoGainParameter
    with HasMaxMemoryInMBParameter
    with HasCacheNodeIdsParameter
    with HasCheckpointIntervalParameter
    with HasSubsamplingRateParameter
    with HasSeedParameter
    with HasNumTreesParameter
    with HasFeatureSubsetStrategyParameter
    with PredictorParameters
    with HasLabelColumnParameter
    with ProbabilisticClassifierParameters
    with HasClassificationImpurityParameter {

  val id = "F237228D-0C36-454B-A562-04939D65B1FB"

  override val parameterGroups = List(ParameterGroup("",
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
    probabilityColumnParameter,
    rawPredictionColumnParameter,
    predictionColumnParameter
  ))
}

object RandomForestClassifierInfo extends RandomForestClassifierInfo