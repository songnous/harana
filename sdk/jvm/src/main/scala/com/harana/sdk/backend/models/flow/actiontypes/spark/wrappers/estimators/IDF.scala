package com.harana.sdk.backend.models.flow.actiontypes.spark.wrappers.estimators

import com.harana.sdk.backend.models.flow.actionobjects.spark.wrappers.estimators.IDFEstimator
import com.harana.sdk.backend.models.flow.actionobjects.spark.wrappers.models.IDFModel
import com.harana.sdk.backend.models.flow.actiontypes.EstimatorAsActionType
import com.harana.sdk.shared.models.flow.actiontypes.spark.wrappers.estimators.IDFInfo
import com.harana.sdk.shared.models.flow.documentation.SparkActionDocumentation

class IDF extends EstimatorAsActionType[IDFEstimator, IDFModel]
  with IDFInfo
  with SparkActionDocumentation