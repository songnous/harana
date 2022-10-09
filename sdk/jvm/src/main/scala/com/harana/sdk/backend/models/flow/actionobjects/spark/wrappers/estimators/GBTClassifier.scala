package com.harana.sdk.backend.models.flow.actionobjects.spark.wrappers.estimators

import com.harana.sdk.backend.models.flow.actionobjects.SparkEstimatorWrapper
import com.harana.sdk.backend.models.flow.actionobjects.spark.wrappers.models.{GBTClassificationModel, VanillaGBTClassificationModel}
import com.harana.sdk.backend.models.flow.actionobjects.stringindexingwrapper.StringIndexingEstimatorWrapper
import com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.estimators.GBTClassifierInfo
import com.harana.sdk.shared.models.flow.utils.TypeUtils
import izumi.reflect.Tag
import org.apache.spark.ml.classification.{GBTClassificationModel => SparkGBTClassificationModel, GBTClassifier => SparkGBTClassifier}

import scala.reflect.runtime.universe._

class GBTClassifier private (vanillaGBTClassifier: VanillaGBTClassifier = new VanillaGBTClassifier())
    extends StringIndexingEstimatorWrapper[
      SparkGBTClassificationModel,
      SparkGBTClassifier,
      VanillaGBTClassificationModel,
      GBTClassificationModel
    ](vanillaGBTClassifier) with GBTClassifierInfo {
  def this() = this(new VanillaGBTClassifier())
}

class VanillaGBTClassifier()
    extends SparkEstimatorWrapper[SparkGBTClassificationModel, SparkGBTClassifier, VanillaGBTClassificationModel]
    with GBTClassifierInfo {

  val estimator = TypeUtils.instanceOfType(Tag[SparkGBTClassifier])
  override def estimatorName = classOf[GBTClassifier].getSimpleName
}