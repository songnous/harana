package com.harana.sdk.shared.models.flow.actiontypes.spark.wrappers.evaluators

import com.harana.sdk.shared.models.common.Version
import com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.evaluators.BinaryClassificationEvaluatorInfo
import com.harana.sdk.shared.models.flow.actiontypes.EvaluatorAsFactoryInfo
import com.harana.sdk.shared.models.flow.catalogs.ActionCategory.ML.ModelEvaluation
import com.harana.sdk.shared.models.flow.utils.Id

trait CreateBinaryClassificationEvaluatorInfo extends EvaluatorAsFactoryInfo[BinaryClassificationEvaluatorInfo] {

  val id: Id = "464ce3fa-e915-4a5d-a9d1-442c1e4b7aa7"
  val name = "binary-classification-evaluator"
  val since = Version(1,0,0)
  val category = ModelEvaluation

  lazy val portO_0 = typeTag[BinaryClassificationEvaluatorInfo]

}

object CreateBinaryClassificationEvaluatorInfo extends CreateBinaryClassificationEvaluatorInfo