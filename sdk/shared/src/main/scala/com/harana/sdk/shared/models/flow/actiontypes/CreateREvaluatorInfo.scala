package com.harana.sdk.shared.models.flow.actiontypes

import com.harana.sdk.shared.models.common.Version
import com.harana.sdk.shared.models.flow.actionobjects.REvaluatorInfo
import com.harana.sdk.shared.models.flow.catalogs.ActionCategory.ML.ModelEvaluation
import com.harana.sdk.shared.models.flow.documentation.ActionDocumentation
import com.harana.sdk.shared.models.flow.utils.Id

import izumi.reflect.Tag

trait CreateREvaluatorInfo extends EvaluatorAsFactoryInfo[REvaluatorInfo] with ActionDocumentation {

  val id: Id = "1c626513-f266-4458-8499-29cbad95bb8c"
  val name = "r-evaluator"
  val since = Version(1, 3, 0)
  val category = ModelEvaluation

  lazy val portO_0: Tag[REvaluatorInfo] = typeTag

}

object CreateREvaluatorInfo extends CreateREvaluatorInfo