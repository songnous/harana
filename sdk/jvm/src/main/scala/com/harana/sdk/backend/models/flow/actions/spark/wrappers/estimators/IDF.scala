package com.harana.sdk.backend.models.flow.actions.spark.wrappers.estimators

import com.harana.sdk.backend.models.designer.flow.actionobjects.spark.wrappers.estimators.IDFEstimator
import com.harana.sdk.backend.models.designer.flow.actionobjects.spark.wrappers.models.IDFModel
import com.harana.sdk.backend.models.designer.flow.actions.EstimatorAsAction
import com.harana.sdk.backend.models.flow.actionobjects.spark.wrappers.estimators.IDFEstimator
import com.harana.sdk.backend.models.flow.actionobjects.spark.wrappers.models.IDFModel
import com.harana.sdk.shared.models.flow.actions.spark.wrappers.estimators.IDFInfo
import com.harana.sdk.shared.models.flow.documentation.SparkActionDocumentation

class IDF extends EstimatorAsAction[IDFEstimator, IDFModel]
  with IDFInfo
  with SparkActionDocumentation