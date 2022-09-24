package com.harana.sdk.shared.models.flow.actions

import com.harana.sdk.shared.models.common.Version
import com.harana.sdk.shared.models.flow.Action2To1Info
import com.harana.sdk.shared.models.flow.catalogs.ActionCategory.Action
import com.harana.sdk.shared.models.flow.actionobjects.{DataFrameInfo, TransformerInfo}
import com.harana.sdk.shared.models.flow.actions.layout.SmallBlockLayout2To1
import com.harana.sdk.shared.models.flow.catalogs.ActionCategory.Action
import com.harana.sdk.shared.models.flow.documentation.ActionDocumentation
import com.harana.sdk.shared.models.flow.parameters.DynamicParameter
import com.harana.sdk.shared.models.flow.utils.Id
import io.circe.Json

import scala.reflect.runtime.universe.TypeTag

trait TransformInfo extends Action2To1Info[TransformerInfo, DataFrameInfo, DataFrameInfo] with SmallBlockLayout2To1 with ActionDocumentation {

  val id: Id = "643d8706-24db-4674-b5b4-10b5129251fc"
  val name = "Transform"
  val since = Version(1, 0, 0)
  val category = Action

  val transformerParameters = new DynamicParameter("Parameters of input Transformer", default = Some(Json.Null), inputPort = 0)
  def getTransformerParameters = $(transformerParameters)
  def setTransformerParameters(jsValue: Json): this.type = set(transformerParameters, jsValue)

  override val parameters =  Left(List(transformerParameters))

  lazy val portI_0: TypeTag[TransformerInfo] = typeTag
  lazy val portI_1: TypeTag[DataFrameInfo] = typeTag
  lazy val portO_0: TypeTag[DataFrameInfo] = typeTag

}

object TransformInfo extends TransformInfo {
  def apply(pos: (Int, Int), color: Option[String] = None) = new TransformInfo {
    override val position = Some(pos)
    override val overrideColor = color
  }
}