package com.harana.sdk.backend.models.flow.actiontypes.spark.wrappers.estimators

import com.harana.sdk.backend.models.flow.actionobjects.spark.wrappers.estimators.MinMaxScalerEstimator
import com.harana.sdk.backend.models.flow.actionobjects.spark.wrappers.models.MinMaxScalerModel
import com.harana.sdk.backend.models.flow.actiontypes.EstimatorAsActionType
import com.harana.sdk.shared.models.flow.actiontypes.spark.wrappers.estimators.MinMaxScalerInfo
import com.harana.sdk.shared.models.flow.documentation.SparkActionDocumentation

class MinMaxScaler extends EstimatorAsActionType[MinMaxScalerEstimator, MinMaxScalerModel]
  with MinMaxScalerInfo
  with SparkActionDocumentation