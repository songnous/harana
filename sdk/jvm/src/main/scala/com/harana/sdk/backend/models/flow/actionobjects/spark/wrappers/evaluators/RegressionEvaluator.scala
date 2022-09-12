package com.harana.sdk.backend.models.flow.actionobjects.spark.wrappers.evaluators

import com.harana.sdk.backend.models.flow.actionobjects.SparkEvaluatorWrapper
import com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.evaluators.RegressionEvaluatorInfo
import org.apache.spark.ml.evaluation.{RegressionEvaluator => SparkRegressionEvaluator}

class RegressionEvaluator extends SparkEvaluatorWrapper[SparkRegressionEvaluator]
  with RegressionEvaluatorInfo