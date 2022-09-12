package com.harana.sdk.backend.models.flow.actionobjects.spark.wrappers.estimators

import com.harana.sdk.backend.models.flow.actionobjects.SparkSingleColumnParameterEstimatorWrapper
import com.harana.sdk.backend.models.flow.actionobjects.spark.wrappers.models.PCAModel
import com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.estimators.PCAEstimatorInfo
import org.apache.spark.ml.feature.{PCA => SparkPCA, PCAModel => SparkPCAModel}

class PCAEstimator
  extends SparkSingleColumnParameterEstimatorWrapper[SparkPCAModel, SparkPCA, PCAModel]
    with PCAEstimatorInfo