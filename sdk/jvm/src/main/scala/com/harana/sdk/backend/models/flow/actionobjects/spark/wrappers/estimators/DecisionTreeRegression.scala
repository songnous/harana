package com.harana.sdk.backend.models.flow.actionobjects.spark.wrappers.estimators

import com.harana.sdk.backend.models.flow.actionobjects.SparkEstimatorWrapper
import com.harana.sdk.backend.models.flow.actionobjects.spark.wrappers.models.DecisionTreeRegressionModel
import com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.estimators.DecisionTreeRegressionInfo
import com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.parameters.DecisionTreeParameters
import com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.parameters.common.{HasLabelColumnParameter, HasRegressionImpurityParameter}
import org.apache.spark.ml.regression.{DecisionTreeRegressionModel => SparkDecisionTreeRegressionModel, DecisionTreeRegressor => SparkDecisionTreeRegressor}

class DecisionTreeRegression
    extends SparkEstimatorWrapper[
      SparkDecisionTreeRegressionModel,
      SparkDecisionTreeRegressor,
      DecisionTreeRegressionModel
    ]
    with DecisionTreeRegressionInfo