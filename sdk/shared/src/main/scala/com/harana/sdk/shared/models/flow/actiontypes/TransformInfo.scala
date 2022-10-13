package com.harana.sdk.shared.models.flow.actiontypes

import com.harana.sdk.shared.models.common.Version
import com.harana.sdk.shared.models.flow.Action2To1TypeInfo
import com.harana.sdk.shared.models.flow.actionobjects.{DataFrameInfo, TransformerInfo}
import com.harana.sdk.shared.models.flow.actiontypes.layout.SmallBlockLayout2To1
import com.harana.sdk.shared.models.flow.catalogs.ActionCategory.Action
import com.harana.sdk.shared.models.flow.documentation.ActionDocumentation
import com.harana.sdk.shared.models.flow.parameters.{DynamicParameter, ParameterGroup}
import com.harana.sdk.shared.models.flow.utils.Id
import io.circe.Json

import izumi.reflect.Tag

trait TransformInfo extends Action2To1TypeInfo[TransformerInfo, DataFrameInfo, DataFrameInfo] with SmallBlockLayout2To1 with ActionDocumentation {

  val id: Id = "643d8706-24db-4674-b5b4-10b5129251fc"
  val name = "transform"
  val since = Version(1, 0, 0)
  val category = Action

  val transformerParameters = new DynamicParameter("input-transformer-parameters", default = Some(Json.Null), inputPort = 0)
  def getTransformerParameters = $(transformerParameters)
  def setTransformerParameters(jsValue: Json): this.type = set(transformerParameters, jsValue)

  override val parameterGroups = List(ParameterGroup("", transformerParameters))

  lazy val portI_0: Tag[TransformerInfo] = typeTag
  lazy val portI_1: Tag[DataFrameInfo] = typeTag
  lazy val portO_0: Tag[DataFrameInfo] = typeTag

}

object TransformInfo extends TransformInfo