package com.harana.sdk.backend.models.flow.actions.spark.wrappers.estimators

import com.harana.sdk.backend.models.flow.actionobjects.spark.wrappers.estimators.StandardScalerEstimator
import com.harana.sdk.backend.models.flow.actionobjects.spark.wrappers.models.StandardScalerModel
import com.harana.sdk.backend.models.flow.actions.EstimatorAsAction
import com.harana.sdk.backend.models.flow.actionobjects.spark.wrappers.estimators.StandardScalerEstimator
import com.harana.sdk.backend.models.flow.actionobjects.spark.wrappers.models.StandardScalerModel
import com.harana.sdk.shared.models.flow.actions.spark.wrappers.estimators.StandardScalerInfo
import com.harana.sdk.shared.models.flow.documentation.SparkActionDocumentation

class StandardScaler extends EstimatorAsAction[StandardScalerEstimator, StandardScalerModel]
  with StandardScalerInfo
  with SparkActionDocumentation