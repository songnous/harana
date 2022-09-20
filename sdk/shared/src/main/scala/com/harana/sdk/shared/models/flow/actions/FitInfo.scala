package com.harana.sdk.shared.models.flow.actions

import com.harana.sdk.shared.models.common.Version
import com.harana.sdk.shared.models.flow.Action2To1Info
import com.harana.sdk.shared.models.flow.actionobjects.{EstimatorInfo, TransformerInfo}
import com.harana.sdk.shared.models.flow.catalogs.ActionCategory.Action
import com.harana.sdk.shared.models.flow.actions.dataframe.DataFrameInfo
import com.harana.sdk.shared.models.flow.actions.layout.SmallBlockLayout2To1
import com.harana.sdk.shared.models.flow.catalogs.ActionCategory.Action
import com.harana.sdk.shared.models.flow.documentation.ActionDocumentation
import com.harana.sdk.shared.models.flow.parameters.DynamicParameter
import com.harana.sdk.shared.models.flow.utils.Id
import io.circe.Json

import scala.reflect.runtime.universe.TypeTag

trait FitInfo
  extends Action2To1Info[EstimatorInfo, DataFrameInfo, TransformerInfo]
    with SmallBlockLayout2To1
    with ActionDocumentation {

  val id: Id = "0c2ff818-977b-11e5-8994-feff819cdc9f"
  val name = "Fit"
  val since = Version(1,0,0)
  val category = Action

  val estimatorParameters = new DynamicParameter("Parameters of input Estimator", inputPort = 0)
  setDefault(estimatorParameters -> Json.Null)
  def setEstimatorParameters(jsValue: Json): this.type = set(estimatorParameters -> jsValue)
  override val parameters =  Array(estimatorParameters)

  lazy val portI_0: TypeTag[EstimatorInfo] = typeTag
  lazy val portI_1: TypeTag[DataFrameInfo] = typeTag
  lazy val portO_0: TypeTag[TransformerInfo] = typeTag

}

object FitInfo extends FitInfo