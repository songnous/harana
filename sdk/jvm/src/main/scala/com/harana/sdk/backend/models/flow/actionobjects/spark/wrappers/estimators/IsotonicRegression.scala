package com.harana.sdk.backend.models.flow.actionobjects.spark.wrappers.estimators

import com.harana.sdk.backend.models.flow.actionobjects.SparkEstimatorWrapper
import com.harana.sdk.backend.models.flow.actionobjects.spark.wrappers.models.IsotonicRegressionModel
import com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.estimators.IsotonicRegressionInfo
import org.apache.spark.ml.regression.{IsotonicRegression => SparkIsotonicRegression, IsotonicRegressionModel => SparkIsotonicRegressionModel}

class IsotonicRegression
    extends SparkEstimatorWrapper[SparkIsotonicRegressionModel, SparkIsotonicRegression, IsotonicRegressionModel]
    with IsotonicRegressionInfo