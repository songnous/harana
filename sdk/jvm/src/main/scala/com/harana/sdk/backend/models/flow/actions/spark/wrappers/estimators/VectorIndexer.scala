package com.harana.sdk.backend.models.flow.actions.spark.wrappers.estimators

import com.harana.sdk.backend.models.flow.actionobjects.spark.wrappers.estimators.VectorIndexerEstimator
import com.harana.sdk.backend.models.flow.actionobjects.spark.wrappers.models.VectorIndexerModel
import com.harana.sdk.backend.models.flow.actions.EstimatorAsAction
import com.harana.sdk.shared.models.flow.actions.spark.wrappers.estimators.VectorIndexerInfo
import com.harana.sdk.shared.models.flow.documentation.SparkActionDocumentation

class VectorIndexer extends EstimatorAsAction[VectorIndexerEstimator, VectorIndexerModel]
  with VectorIndexerInfo
  with SparkActionDocumentation