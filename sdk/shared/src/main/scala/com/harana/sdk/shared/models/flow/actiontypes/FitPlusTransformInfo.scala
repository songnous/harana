package com.harana.sdk.shared.models.flow.actiontypes

import com.harana.sdk.shared.models.common.Version
import com.harana.sdk.shared.models.flow.Action2To2TypeInfo
import com.harana.sdk.shared.models.flow.actionobjects.{DataFrameInfo, EstimatorInfo, TransformerInfo}
import com.harana.sdk.shared.models.flow.actiontypes.layout.SmallBlockLayout2To2
import com.harana.sdk.shared.models.flow.catalogs.ActionCategory.Action
import com.harana.sdk.shared.models.flow.documentation.ActionDocumentation
import com.harana.sdk.shared.models.flow.parameters.{DynamicParameter, ParameterGroup}
import com.harana.sdk.shared.models.flow.utils.Id
import io.circe.Json
import izumi.reflect.Tag

import scala.reflect.runtime.universe._

trait FitPlusTransformInfo
    extends Action2To2TypeInfo[EstimatorInfo, DataFrameInfo, DataFrameInfo, TransformerInfo]
    with SmallBlockLayout2To2
    with ActionDocumentation {

  val id: Id = "1cb153f1-3731-4046-a29b-5ad64fde093f"
  val name = "fit-plus-transform"
  val since = Version(1, 2, 0)
  val category = Action

  lazy val portI_0: Tag[EstimatorInfo] = typeTag[EstimatorInfo]
  lazy val portI_1: Tag[DataFrameInfo] = Tag[DataFrameInfo]
  lazy val portO_0: Tag[DataFrameInfo] = Tag[DataFrameInfo]
  lazy val portO_1: Tag[TransformerInfo] = typeTag[TransformerInfo]

  val estimatorParameters = new DynamicParameter("input-estimator-parameters", default = Some(Json.Null), inputPort = 0)
  def setEstimatorParameters(jsValue: Json): this.type = set(estimatorParameters -> jsValue)

  override val parameterGroups = List(ParameterGroup("", estimatorParameters))

}

object FitPlusTransformInfo extends FitPlusTransformInfo
