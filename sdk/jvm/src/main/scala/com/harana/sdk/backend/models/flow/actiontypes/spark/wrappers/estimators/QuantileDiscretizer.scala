package com.harana.sdk.backend.models.flow.actiontypes.spark.wrappers.estimators

import com.harana.sdk.backend.models.flow.actionobjects.spark.wrappers.estimators.QuantileDiscretizerEstimator
import com.harana.sdk.backend.models.flow.actionobjects.spark.wrappers.models.QuantileDiscretizerModel
import com.harana.sdk.backend.models.flow.actiontypes.EstimatorAsActionType
import com.harana.sdk.backend.models.flow.actionobjects.spark.wrappers.estimators.QuantileDiscretizerEstimator
import com.harana.sdk.backend.models.flow.actionobjects.spark.wrappers.models.QuantileDiscretizerModel
import com.harana.sdk.shared.models.flow.actiontypes.spark.wrappers.estimators.QuantileDiscretizerInfo
import com.harana.sdk.shared.models.flow.documentation.SparkActionDocumentation

class QuantileDiscretizer extends EstimatorAsActionType[QuantileDiscretizerEstimator, QuantileDiscretizerModel]
  with QuantileDiscretizerInfo
  with SparkActionDocumentation