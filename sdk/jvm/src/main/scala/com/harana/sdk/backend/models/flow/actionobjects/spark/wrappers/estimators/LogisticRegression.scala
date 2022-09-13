package com.harana.sdk.backend.models.flow.actionobjects.spark.wrappers.estimators

import com.harana.sdk.backend.models.flow.actionobjects.SparkEstimatorWrapper
import com.harana.sdk.backend.models.flow.actionobjects.spark.wrappers.models.LogisticRegressionModel
import com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.estimators.LogisticRegressionInfo
import org.apache.spark.ml.classification.{LogisticRegression => SparkLogisticRegression, LogisticRegressionModel => SparkLogisticRegressionModel}

class LogisticRegression
    extends SparkEstimatorWrapper[SparkLogisticRegressionModel, SparkLogisticRegression, LogisticRegressionModel]
    with LogisticRegressionInfo
