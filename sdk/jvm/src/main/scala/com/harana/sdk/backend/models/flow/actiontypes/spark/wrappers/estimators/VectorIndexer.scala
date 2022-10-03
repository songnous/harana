package com.harana.sdk.backend.models.flow.actiontypes.spark.wrappers.estimators

import com.harana.sdk.backend.models.flow.actionobjects.spark.wrappers.estimators.VectorIndexerEstimator
import com.harana.sdk.backend.models.flow.actionobjects.spark.wrappers.models.VectorIndexerModel
import com.harana.sdk.backend.models.flow.actiontypes.EstimatorAsActionType
import com.harana.sdk.shared.models.flow.actiontypes.spark.wrappers.estimators.VectorIndexerInfo
import com.harana.sdk.shared.models.flow.documentation.SparkActionDocumentation

class VectorIndexer extends EstimatorAsActionType[VectorIndexerEstimator, VectorIndexerModel]
  with VectorIndexerInfo
  with SparkActionDocumentation