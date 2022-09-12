package com.harana.sdk.backend.models.flow.actionobjects.spark.wrappers.estimators

import com.harana.sdk.backend.models.flow.actionobjects.SparkSingleColumnParameterEstimatorWrapper
import com.harana.sdk.backend.models.flow.actionobjects.spark.wrappers.models.VectorIndexerModel
import com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.estimators.VectorIndexerEstimatorInfo
import org.apache.spark.ml.feature.{VectorIndexer => SparkVectorIndexer}
import org.apache.spark.ml.feature.{VectorIndexerModel => SparkVectorIndexerModel}

class VectorIndexerEstimator
  extends SparkSingleColumnParameterEstimatorWrapper[SparkVectorIndexerModel, SparkVectorIndexer, VectorIndexerModel]
  with VectorIndexerEstimatorInfo