package com.harana.sdk.backend.models.flow.actiontypes.spark.wrappers.estimators

import com.harana.sdk.backend.models.flow.actionobjects.spark.wrappers.estimators.StandardScalerEstimator
import com.harana.sdk.backend.models.flow.actionobjects.spark.wrappers.models.StandardScalerModel
import com.harana.sdk.backend.models.flow.actiontypes.EstimatorAsActionType
import com.harana.sdk.backend.models.flow.actionobjects.spark.wrappers.estimators.StandardScalerEstimator
import com.harana.sdk.backend.models.flow.actionobjects.spark.wrappers.models.StandardScalerModel
import com.harana.sdk.shared.models.flow.actiontypes.spark.wrappers.estimators.StandardScalerInfo
import com.harana.sdk.shared.models.flow.documentation.SparkActionDocumentation

class StandardScaler extends EstimatorAsActionType[StandardScalerEstimator, StandardScalerModel]
  with StandardScalerInfo
  with SparkActionDocumentation