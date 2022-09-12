package com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.parameters.common

import com.harana.sdk.shared.models.flow.parameters.Parameters

import scala.language.reflectiveCalls

trait PredictorParameters extends Parameters
  with HasFeaturesColumnParameter
  with HasPredictionColumnCreatorParameter
