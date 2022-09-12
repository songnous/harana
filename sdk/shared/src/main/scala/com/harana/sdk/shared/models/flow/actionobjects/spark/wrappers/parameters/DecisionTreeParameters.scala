package com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.parameters

import com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.parameters.common._
import com.harana.sdk.shared.models.flow.parameters.Parameters

import scala.language.reflectiveCalls

trait DecisionTreeParameters
  extends Parameters
  with PredictorParameters
  with HasCheckpointIntervalParameter
  with HasSeedParameter
  with HasMaxDepthParameter
  with HasMaxBinsParameter
  with HasMinInstancePerNodeParameter
  with HasMinInfoGainParameter
  with HasMaxMemoryInMBParameter
  with HasCacheNodeIdsParameter