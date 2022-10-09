package com.harana.sdk.shared.models.flow.actiontypes.spark.wrappers.evaluators

import com.harana.sdk.shared.models.common.Version
import com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.evaluators.RegressionEvaluatorInfo
import com.harana.sdk.shared.models.flow.actiontypes.EvaluatorAsFactoryInfo
import com.harana.sdk.shared.models.flow.catalogs.ActionCategory.ML.ModelEvaluation
import com.harana.sdk.shared.models.flow.documentation.SparkActionDocumentation
import com.harana.sdk.shared.models.flow.utils.Id

import izumi.reflect.Tag

trait CreateRegressionEvaluatorInfo extends EvaluatorAsFactoryInfo[RegressionEvaluatorInfo]
  with SparkActionDocumentation {

  val id: Id = "d9c3026c-a3d0-4365-8d1a-464a656b72de"
  val name = "regression-evaluator"
  val since = Version(1,0,0)
  val docsGuideLocation = Some("mllib-evaluation-metrics.html#regression-model-evaluation")
  val category = ModelEvaluation

  lazy val portO_0: Tag[RegressionEvaluatorInfo] = typeTag

}

object CreateRegressionEvaluatorInfo extends CreateRegressionEvaluatorInfo