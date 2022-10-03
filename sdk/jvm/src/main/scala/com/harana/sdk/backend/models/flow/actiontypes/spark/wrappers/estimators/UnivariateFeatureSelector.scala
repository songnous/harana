package com.harana.sdk.backend.models.flow.actiontypes.spark.wrappers.estimators

import com.harana.sdk.backend.models.flow.actionobjects.spark.wrappers.estimators.UnivariateFeatureSelectorEstimator
import com.harana.sdk.backend.models.flow.actionobjects.spark.wrappers.models.UnivariateFeatureSelectorModel
import com.harana.sdk.backend.models.flow.actiontypes.EstimatorAsActionType
import com.harana.sdk.backend.models.flow.actionobjects.spark.wrappers.estimators.UnivariateFeatureSelectorEstimator
import com.harana.sdk.backend.models.flow.actionobjects.spark.wrappers.models.UnivariateFeatureSelectorModel
import com.harana.sdk.shared.models.flow.actiontypes.spark.wrappers.estimators.UnivariateFeatureSelectorInfo
import com.harana.sdk.shared.models.flow.documentation.SparkActionDocumentation

class UnivariateFeatureSelector extends EstimatorAsActionType[UnivariateFeatureSelectorEstimator, UnivariateFeatureSelectorModel]
  with UnivariateFeatureSelectorInfo
  with SparkActionDocumentation