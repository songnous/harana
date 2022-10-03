package com.harana.sdk.backend.models.flow.actiontypes.spark.wrappers.evaluators

import com.harana.sdk.backend.models.flow.actionobjects.spark.wrappers.evaluators.BinaryClassificationEvaluator
import com.harana.sdk.backend.models.flow.actiontypes.EvaluatorAsFactory
import com.harana.sdk.backend.models.flow.actionobjects.spark.wrappers.evaluators.BinaryClassificationEvaluator
import com.harana.sdk.shared.models.flow.actiontypes.spark.wrappers.evaluators.CreateBinaryClassificationEvaluatorInfo

class CreateBinaryClassificationEvaluator extends EvaluatorAsFactory[BinaryClassificationEvaluator]
  with CreateBinaryClassificationEvaluatorInfo