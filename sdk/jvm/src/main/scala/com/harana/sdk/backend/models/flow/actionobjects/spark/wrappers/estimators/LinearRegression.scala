package com.harana.sdk.backend.models.flow.actionobjects.spark.wrappers.estimators

import com.harana.sdk.backend.models.flow.actionobjects.SparkEstimatorWrapper
import com.harana.sdk.backend.models.flow.actionobjects.spark.wrappers.models.LinearRegressionModel
import com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.estimators.LinearRegressionInfo
import com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.parameters.LinearRegressionParameters
import org.apache.spark.ml.regression.{LinearRegression => SparkLinearRegression, LinearRegressionModel => SparkLinearRegressionModel}

class LinearRegression
    extends SparkEstimatorWrapper[SparkLinearRegressionModel, SparkLinearRegression, LinearRegressionModel]
    with LinearRegressionInfo