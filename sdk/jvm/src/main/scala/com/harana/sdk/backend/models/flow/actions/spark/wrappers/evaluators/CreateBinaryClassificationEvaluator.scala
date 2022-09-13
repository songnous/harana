package com.harana.sdk.backend.models.flow.actions.spark.wrappers.evaluators

import com.harana.sdk.backend.models.flow.actionobjects.spark.wrappers.evaluators.BinaryClassificationEvaluator
import com.harana.sdk.backend.models.flow.actions.EvaluatorAsFactory
import com.harana.sdk.backend.models.flow.actionobjects.spark.wrappers.evaluators.BinaryClassificationEvaluator
import com.harana.sdk.shared.models.flow.actions.spark.wrappers.evaluators.CreateBinaryClassificationEvaluatorInfo

class CreateBinaryClassificationEvaluator extends EvaluatorAsFactory[BinaryClassificationEvaluator]
  with CreateBinaryClassificationEvaluatorInfo