package com.harana.sdk.backend.models.flow.actionobjects.spark.wrappers.estimators

import com.harana.sdk.backend.models.flow.actionobjects.SparkEstimatorWrapper
import com.harana.sdk.backend.models.flow.actionobjects.spark.wrappers.models.{DecisionTreeClassificationModel, VanillaDecisionTreeClassificationModel}
import com.harana.sdk.backend.models.flow.actionobjects.stringindexingwrapper.StringIndexingEstimatorWrapper
import com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.estimators.DecisionTreeClassifierInfo
import org.apache.spark.ml.classification.{DecisionTreeClassificationModel => SparkDecisionTreeClassificationModel, DecisionTreeClassifier => SparkDecisionTreeClassifier}

class DecisionTreeClassifier private (val vanillaDecisionTreeClassifier: VanillaDecisionTreeClassifier)
    extends StringIndexingEstimatorWrapper[
      SparkDecisionTreeClassificationModel,
      SparkDecisionTreeClassifier,
      VanillaDecisionTreeClassificationModel,
      DecisionTreeClassificationModel
    ](vanillaDecisionTreeClassifier) with DecisionTreeClassifierInfo {

  def this() = this(new VanillaDecisionTreeClassifier())

}

class VanillaDecisionTreeClassifier
    extends SparkEstimatorWrapper[
      SparkDecisionTreeClassificationModel,
      SparkDecisionTreeClassifier,
      VanillaDecisionTreeClassificationModel
    ]
    with DecisionTreeClassifierInfo {

  override def estimatorName = classOf[DecisionTreeClassifier].getSimpleName
}