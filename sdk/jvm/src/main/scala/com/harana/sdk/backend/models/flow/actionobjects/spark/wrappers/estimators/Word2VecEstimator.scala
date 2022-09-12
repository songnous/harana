package com.harana.sdk.backend.models.flow.actionobjects.spark.wrappers.estimators

import com.harana.sdk.backend.models.flow.actionobjects.SparkSingleColumnParameterEstimatorWrapper
import com.harana.sdk.backend.models.flow.actionobjects.spark.wrappers.models.Word2VecModel
import com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.estimators.Word2VecEstimatorInfo
import com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.parameters.Word2VecParameters
import org.apache.spark.ml.feature.{Word2Vec => SparkWord2Vec, Word2VecModel => SparkWord2VecModel}

class Word2VecEstimator
    extends SparkSingleColumnParameterEstimatorWrapper[SparkWord2VecModel, SparkWord2Vec, Word2VecModel]
    with Word2VecEstimatorInfo