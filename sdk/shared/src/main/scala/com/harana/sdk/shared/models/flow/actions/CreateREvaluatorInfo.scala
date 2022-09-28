package com.harana.sdk.shared.models.flow.actions

import com.harana.sdk.shared.models.common.Version
import com.harana.sdk.shared.models.flow.catalogs.ActionCategory.ML
import com.harana.sdk.shared.models.flow.actionobjects.REvaluatorInfo
import com.harana.sdk.shared.models.flow.catalogs.ActionCategory.ML.ModelEvaluation
import com.harana.sdk.shared.models.flow.documentation.ActionDocumentation
import com.harana.sdk.shared.models.flow.utils.Id

import scala.reflect.runtime.universe.{TypeTag, typeTag}

trait CreateREvaluatorInfo extends EvaluatorAsFactoryInfo[REvaluatorInfo] with ActionDocumentation {

  val id: Id = "1c626513-f266-4458-8499-29cbad95bb8c"
  val name = "R Evaluator"
  val since = Version(1, 3, 0)
  val category = ModelEvaluation

  lazy val portO_0: TypeTag[REvaluatorInfo] = typeTag

}

object CreateREvaluatorInfo extends CreateREvaluatorInfo with UIActionInfo[CreateREvaluatorInfo] {
  def apply(pos: (Int, Int), color: Option[String] = None) = new CreateREvaluatorInfo {
    override val position = Some(pos)
    override val overrideColor = color
  }
}