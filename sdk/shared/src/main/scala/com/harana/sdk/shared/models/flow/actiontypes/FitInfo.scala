package com.harana.sdk.shared.models.flow.actiontypes

import com.harana.sdk.shared.models.common.Version
import com.harana.sdk.shared.models.flow.Action2To1TypeInfo
import com.harana.sdk.shared.models.flow.actionobjects.{DataFrameInfo, EstimatorInfo, TransformerInfo}
import com.harana.sdk.shared.models.flow.actiontypes.layout.SmallBlockLayout2To1
import com.harana.sdk.shared.models.flow.catalogs.ActionCategory.Action
import com.harana.sdk.shared.models.flow.documentation.ActionDocumentation
import com.harana.sdk.shared.models.flow.parameters.{DynamicParameter, ParameterGroup}
import com.harana.sdk.shared.models.flow.utils.Id
import io.circe.Json

import izumi.reflect.Tag

trait FitInfo
  extends Action2To1TypeInfo[EstimatorInfo, DataFrameInfo, TransformerInfo]
    with SmallBlockLayout2To1
    with ActionDocumentation {

  val id: Id = "0c2ff818-977b-11e5-8994-feff819cdc9f"
  val name = "fit"
  val since = Version(1,0,0)
  val category = Action

  val estimatorParameters = new DynamicParameter("input-estimator-parameters", default = Some(Json.Null), inputPort = 0)
  def setEstimatorParameters(jsValue: Json): this.type = set(estimatorParameters -> jsValue)
  override val parameterGroups = List(ParameterGroup("", estimatorParameters))

  lazy val portI_0: Tag[EstimatorInfo] = typeTag
  lazy val portI_1: Tag[DataFrameInfo] = typeTag
  lazy val portO_0: Tag[TransformerInfo] = typeTag

}

object FitInfo extends FitInfo