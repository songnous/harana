package com.harana.sdk.backend.models.flow.actions.spark.wrappers.evaluators

import com.harana.sdk.backend.models.designer.flow.actionobjects.spark.wrappers.evaluators.MulticlassClassificationEvaluator
import com.harana.sdk.backend.models.designer.flow.actions.EvaluatorAsFactory
import com.harana.sdk.backend.models.flow.actionobjects.spark.wrappers.evaluators.MulticlassClassificationEvaluator
import com.harana.sdk.shared.models.flow.actions.spark.wrappers.evaluators.CreateMulticlassClassificationEvaluatorInfo
import com.harana.sdk.shared.models.flow.documentation.SparkActionDocumentation

class CreateMulticlassClassificationEvaluator extends EvaluatorAsFactory[MulticlassClassificationEvaluator]
  with CreateMulticlassClassificationEvaluatorInfo
  with SparkActionDocumentation