package com.harana.sdk.shared.models.flow.actions

import com.harana.sdk.shared.models.common.Version
import com.harana.sdk.shared.models.flow.Action2To1Info
import com.harana.sdk.shared.models.flow.catalogs.ActionCategory.Action
import com.harana.sdk.shared.models.flow.actionobjects.{DataFrameInfo, EvaluatorInfo, MetricValue}
import com.harana.sdk.shared.models.flow.actions.layout.SmallBlockLayout2To1
import com.harana.sdk.shared.models.flow.catalogs.ActionCategory.Action
import com.harana.sdk.shared.models.flow.documentation.ActionDocumentation
import com.harana.sdk.shared.models.flow.parameters.{DynamicParameter, ParameterGroup}
import com.harana.sdk.shared.models.flow.utils.Id
import io.circe.Json

import scala.reflect.runtime.universe.TypeTag

trait EvaluateInfo extends Action2To1Info[EvaluatorInfo, DataFrameInfo, MetricValue] with SmallBlockLayout2To1 with ActionDocumentation {

  val id: Id = "a88eaf35-9061-4714-b042-ddd2049ce917"
  val name = "Evaluate"
  val since = Version(1, 0, 0)
  val category = Action

  val evaluatorParameters = new DynamicParameter("Parameters of input Evaluator", default = Some(Json.Null), inputPort = 0)
  def getEvaluatorParameters = $(evaluatorParameters)
  def setEvaluatorParameters(jsValue: Json): this.type = set(evaluatorParameters, jsValue)

  override val parameterGroups = List(ParameterGroup(None, evaluatorParameters))

  lazy val portI_0: TypeTag[EvaluatorInfo] = typeTag
  lazy val portI_1: TypeTag[DataFrameInfo] = typeTag
  lazy val portO_0: TypeTag[MetricValue] = typeTag

}

object EvaluateInfo extends EvaluateInfo with UIActionInfo[EvaluateInfo] {
  def apply(pos: (Int, Int), color: Option[String] = None) = new EvaluateInfo {
    override val position = Some(pos)
    override val overrideColor = color
  }
}