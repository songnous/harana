package com.harana.sdk.backend.models.flow.actions.spark.wrappers.estimators

import com.harana.sdk.backend.models.designer.flow.actionobjects.spark.wrappers.estimators.MinMaxScalerEstimator
import com.harana.sdk.backend.models.designer.flow.actionobjects.spark.wrappers.models.MinMaxScalerModel
import com.harana.sdk.backend.models.designer.flow.actions.EstimatorAsAction
import com.harana.sdk.backend.models.flow.actionobjects.spark.wrappers.estimators.MinMaxScalerEstimator
import com.harana.sdk.backend.models.flow.actionobjects.spark.wrappers.models.MinMaxScalerModel
import com.harana.sdk.shared.models.common.Version
import com.harana.sdk.shared.models.flow.actions.spark.wrappers.estimators.MinMaxScalerInfo
import com.harana.sdk.shared.models.flow.documentation.SparkActionDocumentation
import com.harana.sdk.shared.models.flow.utils.Id

class MinMaxScaler extends EstimatorAsAction[MinMaxScalerEstimator, MinMaxScalerModel]
  with MinMaxScalerInfo
  with SparkActionDocumentation