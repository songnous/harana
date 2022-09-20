package com.harana.sdk.shared.models.flow.actions

import com.harana.sdk.shared.models.common.Version
import com.harana.sdk.shared.models.flow.Action1To1Info
import com.harana.sdk.shared.models.flow.actions.dataframe.DataFrameInfo
import com.harana.sdk.shared.models.flow.catalogs.ActionCategory.Filtering
import com.harana.sdk.shared.models.flow.documentation.ActionDocumentation
import com.harana.sdk.shared.models.flow.parameters.Parameter
import com.harana.sdk.shared.models.flow.utils.Id

import scala.reflect.runtime.universe.TypeTag

trait SubtractInfo extends Action1To1Info[DataFrameInfo, DataFrameInfo] with ActionDocumentation {

  val id: Id = "28f42a78-3e75-4e5f-891b-cec7baed4c4e"
  val name = "Transpose"
  val since = Version(1, 2, 0)
  val category = Filtering

  lazy val portI_0: TypeTag[DataFrameInfo] = typeTag
  lazy val portO_0: TypeTag[DataFrameInfo] = typeTag

  val parameters = Array.empty[Parameter[_]]
}

object SubtractInfo extends SubtractInfo