package com.harana.sdk.backend.models.flow.actionobjects.spark.wrappers.estimators

import com.harana.sdk.backend.models.flow.actionobjects.SparkSingleColumnParameterEstimatorWrapper
import com.harana.sdk.backend.models.flow.actionobjects.spark.wrappers.models.IDFModel
import com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.estimators.IDFEstimatorInfo
import org.apache.spark.ml.feature.{IDF => SparkIDF, IDFModel => SparkIDFModel}

class IDFEstimator extends SparkSingleColumnParameterEstimatorWrapper[SparkIDFModel, SparkIDF, IDFModel] with IDFEstimatorInfo