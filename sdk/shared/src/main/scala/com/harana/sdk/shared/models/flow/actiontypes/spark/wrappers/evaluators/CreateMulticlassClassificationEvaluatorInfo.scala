package com.harana.sdk.shared.models.flow.actiontypes.spark.wrappers.evaluators

import com.harana.sdk.shared.models.common.Version
import com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.evaluators.MulticlassClassificationEvaluatorInfo
import com.harana.sdk.shared.models.flow.actiontypes.EvaluatorAsFactoryInfo
import com.harana.sdk.shared.models.flow.catalogs.ActionCategory.ML.ModelEvaluation
import com.harana.sdk.shared.models.flow.documentation.SparkActionDocumentation
import com.harana.sdk.shared.models.flow.utils.Id
import izumi.reflect.Tag

trait CreateMulticlassClassificationEvaluatorInfo extends EvaluatorAsFactoryInfo[MulticlassClassificationEvaluatorInfo]
  with SparkActionDocumentation {

  val id: Id = "3129848c-8a1c-449e-b006-340fec5b42ae"
  val name = "multiclass-classification-evaluator"
  val since = Version(1,0,0)
  val docsGuideLocation = Some("mllib-evaluation-metrics.html#multiclass-classification")
  val category = ModelEvaluation

  lazy val portO_0: Tag[MulticlassClassificationEvaluatorInfo] = typeTag

}

object CreateMulticlassClassificationEvaluatorInfo extends CreateMulticlassClassificationEvaluatorInfo