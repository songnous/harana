package com.harana.sdk.shared.models.flow.actiontypes

import com.harana.sdk.shared.models.common.Version
import com.harana.sdk.shared.models.flow.Action2To1TypeInfo
import com.harana.sdk.shared.models.flow.actionobjects.{DataFrameInfo, EvaluatorInfo, MetricValue}
import com.harana.sdk.shared.models.flow.actiontypes.layout.SmallBlockLayout2To1
import com.harana.sdk.shared.models.flow.catalogs.ActionCategory.Action
import com.harana.sdk.shared.models.flow.documentation.ActionDocumentation
import com.harana.sdk.shared.models.flow.parameters.{DynamicParameter, ParameterGroup}
import com.harana.sdk.shared.models.flow.utils.Id
import io.circe.Json

import izumi.reflect.Tag

trait EvaluateInfo extends Action2To1TypeInfo[EvaluatorInfo, DataFrameInfo, MetricValue] with SmallBlockLayout2To1 with ActionDocumentation {

  val id: Id = "a88eaf35-9061-4714-b042-ddd2049ce917"
  val name = "evaluate"
  val since = Version(1, 0, 0)
  val category = Action

  lazy val portI_0: Tag[EvaluatorInfo] = typeTag
  lazy val portI_1: Tag[DataFrameInfo] = typeTag
  lazy val portO_0: Tag[MetricValue] = typeTag

  val evaluatorParameters = DynamicParameter("input-evaluator-parameters", default = Some(Json.Null), inputPort = 0)
  def getEvaluatorParameters = $(evaluatorParameters)
  def setEvaluatorParameters(jsValue: Json): this.type = set(evaluatorParameters, jsValue)

  override val parameterGroups = List(ParameterGroup("", evaluatorParameters))

}

object EvaluateInfo extends EvaluateInfo