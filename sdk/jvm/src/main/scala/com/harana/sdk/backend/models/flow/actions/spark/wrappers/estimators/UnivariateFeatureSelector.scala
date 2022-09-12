package com.harana.sdk.backend.models.flow.actions.spark.wrappers.estimators

import com.harana.sdk.backend.models.designer.flow.actionobjects.spark.wrappers.estimators.UnivariateFeatureSelectorEstimator
import com.harana.sdk.backend.models.designer.flow.actionobjects.spark.wrappers.models.UnivariateFeatureSelectorModel
import com.harana.sdk.backend.models.designer.flow.actions.EstimatorAsAction
import com.harana.sdk.backend.models.flow.actionobjects.spark.wrappers.estimators.UnivariateFeatureSelectorEstimator
import com.harana.sdk.backend.models.flow.actionobjects.spark.wrappers.models.UnivariateFeatureSelectorModel
import com.harana.sdk.shared.models.flow.actions.spark.wrappers.estimators.UnivariateFeatureSelectorInfo
import com.harana.sdk.shared.models.flow.documentation.SparkActionDocumentation

class UnivariateFeatureSelector extends EstimatorAsAction[UnivariateFeatureSelectorEstimator, UnivariateFeatureSelectorModel]
  with UnivariateFeatureSelectorInfo
  with SparkActionDocumentation