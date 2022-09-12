package com.harana.sdk.backend.models.flow.actionobjects.spark.wrappers.models

import com.harana.sdk.backend.models.flow.actionobjects.{LoadableWithFallback, SparkModelWrapper}
import com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.models.DecisionTreeRegressionModelInfo
import com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.parameters.common.{HasFeaturesColumnParameter, HasPredictionColumnCreatorParameter}
import com.harana.spark.ML
import org.apache.spark.ml.regression.{DecisionTreeRegressionModel => SparkDecisionTreeRegressionModel, DecisionTreeRegressor => SparkDecisionTreeRegressor}

class DecisionTreeRegressionModel
    extends SparkModelWrapper[SparkDecisionTreeRegressionModel, SparkDecisionTreeRegressor]
    with LoadableWithFallback[SparkDecisionTreeRegressionModel, SparkDecisionTreeRegressor]
    with DecisionTreeRegressionModelInfo {

  def tryToLoadModel(path: String) = ML.ModelLoading.decisionTreeRegression(path)
}
