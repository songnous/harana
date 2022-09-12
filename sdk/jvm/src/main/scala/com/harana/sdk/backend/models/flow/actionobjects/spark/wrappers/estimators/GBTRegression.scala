package com.harana.sdk.backend.models.flow.actionobjects.spark.wrappers.estimators

import com.harana.sdk.backend.models.flow.actionobjects.SparkEstimatorWrapper
import com.harana.sdk.backend.models.flow.actionobjects.spark.wrappers.models.GBTRegressionModel
import com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.estimators.GBTRegressionInfo
import com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.parameters.GBTParameters
import org.apache.spark.ml.regression.{GBTRegressionModel => SparkGBTRegressionModel, GBTRegressor => SparkGBTRegressor}

import scala.language.reflectiveCalls

class GBTRegression extends SparkEstimatorWrapper[SparkGBTRegressionModel, SparkGBTRegressor, GBTRegressionModel]
    with GBTParameters
    with GBTRegressionInfo