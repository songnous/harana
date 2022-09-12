package com.harana.sdk.backend.models.flow.actionobjects.spark.wrappers.estimators

import com.harana.sdk.backend.models.flow.actionobjects.SparkEstimatorWrapper
import com.harana.sdk.backend.models.flow.actionobjects.spark.wrappers.models.RandomForestRegressionModel
import com.harana.sdk.backend.models.flow.utils.Logging
import com.harana.sdk.shared.models.designer.flow.actionobjects.spark.wrappers.parameters.common._
import com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.estimators.RandomForestRegressionInfo
import org.apache.spark.ml.regression.{RandomForestRegressionModel => SparkRFRModel, RandomForestRegressor => SparkRFR}

class RandomForestRegression
    extends SparkEstimatorWrapper[SparkRFRModel, SparkRFR, RandomForestRegressionModel]
    with RandomForestRegressionInfo