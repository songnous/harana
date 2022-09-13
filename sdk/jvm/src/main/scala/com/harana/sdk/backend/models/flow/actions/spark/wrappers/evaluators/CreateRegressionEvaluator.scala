package com.harana.sdk.backend.models.flow.actions.spark.wrappers.evaluators

import com.harana.sdk.backend.models.flow.actionobjects.spark.wrappers.evaluators.RegressionEvaluator
import com.harana.sdk.backend.models.flow.actions.EvaluatorAsFactory
import com.harana.sdk.backend.models.flow.actionobjects.spark.wrappers.evaluators.RegressionEvaluator
import com.harana.sdk.shared.models.flow.actions.spark.wrappers.evaluators.CreateRegressionEvaluatorInfo
import com.harana.sdk.shared.models.flow.documentation.SparkActionDocumentation

class CreateRegressionEvaluator extends EvaluatorAsFactory[RegressionEvaluator]
  with CreateRegressionEvaluatorInfo
  with SparkActionDocumentation
