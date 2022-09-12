package com.harana.sdk.backend.models.flow.actionobjects.spark.wrappers.estimators

import com.harana.sdk.backend.models.flow.actionobjects.SparkEstimatorWrapper
import com.harana.sdk.backend.models.flow.actionobjects.spark.wrappers.models.AFTSurvivalRegressionModel
import com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.estimators.AFTSurvivalRegressionInfo
import org.apache.spark.ml.regression.{AFTSurvivalRegression => SparkAFTSurvivalRegression, AFTSurvivalRegressionModel => SparkAFTSurvivalRegressionModel}

import scala.language.reflectiveCalls

class AFTSurvivalRegression
    extends SparkEstimatorWrapper[
      SparkAFTSurvivalRegressionModel,
      SparkAFTSurvivalRegression,
      AFTSurvivalRegressionModel
    ]
    with AFTSurvivalRegressionInfo