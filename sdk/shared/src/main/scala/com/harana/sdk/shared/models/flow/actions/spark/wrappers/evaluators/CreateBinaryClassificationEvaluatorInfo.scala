package com.harana.sdk.shared.models.flow.actions.spark.wrappers.evaluators

import com.harana.sdk.shared.models.common.Version
import com.harana.sdk.shared.models.flow.catalogs.ActionCategory.ML
import com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.evaluators.BinaryClassificationEvaluatorInfo
import com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.transformers.RegexTokenizerInfo
import com.harana.sdk.shared.models.flow.actions.{EvaluatorAsFactoryInfo, UIActionInfo}
import com.harana.sdk.shared.models.flow.actions.spark.wrappers.estimators.UnivariateFeatureSelectorInfo
import com.harana.sdk.shared.models.flow.catalogs.ActionCategory.ML.ModelEvaluation
import com.harana.sdk.shared.models.flow.utils.Id

trait CreateBinaryClassificationEvaluatorInfo extends EvaluatorAsFactoryInfo[BinaryClassificationEvaluatorInfo] {

  val id: Id = "464ce3fa-e915-4a5d-a9d1-442c1e4b7aa7"
  val name = "Binary Classification Evaluator"
  val since = Version(1,0,0)
  val category = ModelEvaluation

  lazy val portO_0 = typeTag[BinaryClassificationEvaluatorInfo]

}

object CreateBinaryClassificationEvaluatorInfo extends CreateBinaryClassificationEvaluatorInfo with UIActionInfo[CreateBinaryClassificationEvaluatorInfo] {
  def apply(pos: (Int, Int), color: Option[String] = None) = new CreateBinaryClassificationEvaluatorInfo {
    override val position = Some(pos)
    override val overrideColor = color
  }
}