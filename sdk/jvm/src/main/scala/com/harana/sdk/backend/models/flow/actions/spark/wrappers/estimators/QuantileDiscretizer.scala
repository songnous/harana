package com.harana.sdk.backend.models.flow.actions.spark.wrappers.estimators

import com.harana.sdk.backend.models.flow.actionobjects.spark.wrappers.estimators.QuantileDiscretizerEstimator
import com.harana.sdk.backend.models.flow.actionobjects.spark.wrappers.models.QuantileDiscretizerModel
import com.harana.sdk.backend.models.flow.actions.EstimatorAsAction
import com.harana.sdk.backend.models.flow.actionobjects.spark.wrappers.estimators.QuantileDiscretizerEstimator
import com.harana.sdk.backend.models.flow.actionobjects.spark.wrappers.models.QuantileDiscretizerModel
import com.harana.sdk.shared.models.flow.actions.spark.wrappers.estimators.QuantileDiscretizerInfo
import com.harana.sdk.shared.models.flow.documentation.SparkActionDocumentation

class QuantileDiscretizer extends EstimatorAsAction[QuantileDiscretizerEstimator, QuantileDiscretizerModel]
  with QuantileDiscretizerInfo
  with SparkActionDocumentation