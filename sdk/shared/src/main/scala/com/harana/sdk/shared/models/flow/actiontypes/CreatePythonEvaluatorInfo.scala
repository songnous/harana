package com.harana.sdk.shared.models.flow.actiontypes

import com.harana.sdk.shared.models.common.Version
import com.harana.sdk.shared.models.flow.actionobjects.PythonEvaluatorInfo
import com.harana.sdk.shared.models.flow.catalogs.ActionCategory.ML.ModelEvaluation
import com.harana.sdk.shared.models.flow.documentation.ActionDocumentation
import com.harana.sdk.shared.models.flow.utils.Id

import izumi.reflect.Tag

trait CreatePythonEvaluatorInfo extends EvaluatorAsFactoryInfo[PythonEvaluatorInfo] with ActionDocumentation {

  val id: Id = "582748ff-b1e4-4821-94da-d6c411e76e7e"
  val name = "python-evaluator"
  val since = Version(1, 2, 0)
  val category = ModelEvaluation

  lazy val portO_0: Tag[PythonEvaluatorInfo] = typeTag

}

object CreatePythonEvaluatorInfo extends CreatePythonEvaluatorInfo