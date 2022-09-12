package com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.parameters

import com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.parameters.common._
import com.harana.sdk.shared.models.flow.parameters.Parameters

import scala.language.reflectiveCalls

trait GBTParameters
  extends Parameters
  with PredictorParameters
  with HasLabelColumnParameter
  with HasMaxIterationsParameter
  with HasSeedParameter
  with HasStepSizeParameter
  with HasMaxBinsParameter
  with HasMaxDepthParameter
  with HasMinInfoGainParameter
  with HasMinInstancePerNodeParameter
  with HasSubsamplingRateParameter