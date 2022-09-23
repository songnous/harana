package com.harana.sdk.shared.models.flow.actions

import com.harana.sdk.shared.models.common.Version
import com.harana.sdk.shared.models.flow.catalogs.ActionCategory.Action
import com.harana.sdk.shared.models.flow.Action2To2Info
import com.harana.sdk.shared.models.flow.actionobjects.{EstimatorInfo, TransformerInfo}
import com.harana.sdk.shared.models.flow.actions.dataframe.DataFrameInfo
import com.harana.sdk.shared.models.flow.actions.layout.SmallBlockLayout2To2
import com.harana.sdk.shared.models.flow.catalogs.ActionCategory.Action
import com.harana.sdk.shared.models.flow.documentation.ActionDocumentation
import com.harana.sdk.shared.models.flow.parameters.DynamicParameter
import com.harana.sdk.shared.models.flow.utils.Id
import io.circe.Json

import scala.reflect.runtime.universe._

trait FitPlusTransformInfo
    extends Action2To2Info[EstimatorInfo, DataFrameInfo, DataFrameInfo, TransformerInfo]
    with SmallBlockLayout2To2
    with ActionDocumentation {

  val id: Id = "1cb153f1-3731-4046-a29b-5ad64fde093f"
  val name = "Fit + Transform"
  val since = Version(1, 2, 0)
  val category = Action

  lazy val portI_0: TypeTag[EstimatorInfo] = typeTag[EstimatorInfo]
  lazy val portI_1: TypeTag[DataFrameInfo] = typeTag[DataFrameInfo]
  lazy val portO_0: TypeTag[DataFrameInfo] = typeTag[DataFrameInfo]
  lazy val portO_1: TypeTag[TransformerInfo] = typeTag[TransformerInfo]

  val estimatorParameters = new DynamicParameter("Parameters of input Estimator", inputPort = 0)
  setDefault(estimatorParameters -> Json.Null)
  def setEstimatorParameters(jsValue: Json): this.type = set(estimatorParameters -> jsValue)

  override val parameters = Array(estimatorParameters)

}

object FitPlusTransformInfo extends FitPlusTransformInfo {
  def apply() = new FitPlusTransformInfo {}
}
