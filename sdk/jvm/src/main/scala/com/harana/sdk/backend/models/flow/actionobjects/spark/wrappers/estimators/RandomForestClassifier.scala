package com.harana.sdk.backend.models.flow.actionobjects.spark.wrappers.estimators

import com.harana.sdk.backend.models.flow.actionobjects.SparkEstimatorWrapper
import com.harana.sdk.backend.models.flow.actionobjects.spark.wrappers.models.{RandomForestClassificationModel, VanillaRandomForestClassificationModel}
import com.harana.sdk.backend.models.flow.actionobjects.stringindexingwrapper.StringIndexingEstimatorWrapper
import com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.estimators.RandomForestClassifierInfo
import org.apache.spark.ml.classification.{RandomForestClassificationModel => SparkRandomForestClassificationModel, RandomForestClassifier => SparkRandomForestClassifier}

class RandomForestClassifier private (val vanillaRandomForestClassifier: VanillaRandomForestClassifier = new VanillaRandomForestClassifier())
    extends StringIndexingEstimatorWrapper[
      SparkRandomForestClassificationModel,
      SparkRandomForestClassifier,
      VanillaRandomForestClassificationModel,
      RandomForestClassificationModel
    ](vanillaRandomForestClassifier) {
  def this() = this(new VanillaRandomForestClassifier())
}

class VanillaRandomForestClassifier
    extends SparkEstimatorWrapper[
      SparkRandomForestClassificationModel,
      SparkRandomForestClassifier,
      VanillaRandomForestClassificationModel
    ] with RandomForestClassifierInfo {

  override def estimatorName = classOf[RandomForestClassifier].getSimpleName
}
