package com.harana.sdk.backend.models.flow.actionobjects.spark.wrappers.models

import com.harana.sdk.backend.models.flow.actionobjects.{LoadableWithFallback, SparkModelWrapper}
import com.harana.sdk.backend.models.flow.actionobjects.stringindexingwrapper.StringIndexingWrapperModel
import com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.models.{DecisionTreeClassificationModelInfo, VanillaDecisionTreeClassificationModelInfo}
import com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.parameters.common.ProbabilisticClassifierParameters
import com.harana.spark.ML
import org.apache.spark.ml.classification.{DecisionTreeClassificationModel => SparkDecisionTreeClassificationModel, DecisionTreeClassifier => SparkDecisionTreeClassifier}

class DecisionTreeClassificationModel(vanillaModel: VanillaDecisionTreeClassificationModel)
    extends StringIndexingWrapperModel[SparkDecisionTreeClassificationModel, SparkDecisionTreeClassifier](vanillaModel)
      with DecisionTreeClassificationModelInfo

class VanillaDecisionTreeClassificationModel
    extends SparkModelWrapper[SparkDecisionTreeClassificationModel, SparkDecisionTreeClassifier]
    with ProbabilisticClassifierParameters
    with LoadableWithFallback[SparkDecisionTreeClassificationModel, SparkDecisionTreeClassifier]
    with VanillaDecisionTreeClassificationModelInfo {

  override def transformerName = classOf[DecisionTreeClassificationModel].getSimpleName

  def tryToLoadModel(path: String) = ML.ModelLoading.decisionTreeClassification(path)
}
