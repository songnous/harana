package com.harana.sdk.shared.models.flow.actions

import com.harana.sdk.shared.models.common.Version
import com.harana.sdk.shared.models.flow.catalogs.ActionCategory.ML
import com.harana.sdk.shared.models.flow.actionobjects.PythonEvaluatorInfo
import com.harana.sdk.shared.models.flow.catalogs.ActionCategory.ML.ModelEvaluation
import com.harana.sdk.shared.models.flow.documentation.ActionDocumentation
import com.harana.sdk.shared.models.flow.parameters.Parameter
import com.harana.sdk.shared.models.flow.utils.Id

import scala.reflect.runtime.universe.{TypeTag, typeTag}

trait CreatePythonEvaluatorInfo extends EvaluatorAsFactoryInfo[PythonEvaluatorInfo] with ActionDocumentation {

  val id: Id = "582748ff-b1e4-4821-94da-d6c411e76e7e"
  val name = "Python Evaluator"
  val since = Version(1, 2, 0)
  val category = ModelEvaluation

  lazy val portO_0: TypeTag[PythonEvaluatorInfo] = typeTag

}

object CreatePythonEvaluatorInfo extends CreatePythonEvaluatorInfo {
  def apply(pos: (Int, Int), color: Option[String] = None) = new CreatePythonEvaluatorInfo {
    override val position = Some(pos)
    override val overrideColor = color
  }
}