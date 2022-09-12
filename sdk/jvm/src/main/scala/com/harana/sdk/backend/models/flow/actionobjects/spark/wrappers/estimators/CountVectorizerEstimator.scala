package com.harana.sdk.backend.models.flow.actionobjects.spark.wrappers.estimators

import com.harana.sdk.backend.models.flow.actionobjects.SparkSingleColumnParameterEstimatorWrapper
import com.harana.sdk.backend.models.flow.actionobjects.spark.wrappers.models.CountVectorizerModel
import com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.estimators.CountVectorizerEstimatorInfo
import org.apache.spark.ml.feature.{CountVectorizer => SparkCountVectorizer, CountVectorizerModel => SparkCountVectorizerModel}

import scala.language.reflectiveCalls

class CountVectorizerEstimator
    extends SparkSingleColumnParameterEstimatorWrapper[SparkCountVectorizerModel, SparkCountVectorizer, CountVectorizerModel]
    with CountVectorizerEstimatorInfo
