package com.harana.sdk.backend.models.flow.actionobjects.spark.wrappers.estimators

import com.harana.sdk.backend.models.flow.actionobjects.SparkEstimatorWrapper
import com.harana.sdk.backend.models.flow.actionobjects.spark.wrappers.models.NaiveBayesModel
import com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.estimators.NaiveBayesInfo
import org.apache.spark.ml.classification.{NaiveBayes => SparkNaiveBayes, NaiveBayesModel => SparkNaiveBayesModel}

import scala.language.reflectiveCalls

class NaiveBayes
    extends SparkEstimatorWrapper[SparkNaiveBayesModel, SparkNaiveBayes, NaiveBayesModel]
    with NaiveBayesInfo