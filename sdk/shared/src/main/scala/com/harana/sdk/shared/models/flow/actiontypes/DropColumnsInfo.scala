package com.harana.sdk.shared.models.flow.actiontypes

import com.harana.sdk.shared.models.common.Version
import com.harana.sdk.shared.models.flow.Action1To1TypeInfo
import com.harana.sdk.shared.models.flow.actionobjects.DataFrameInfo
import com.harana.sdk.shared.models.flow.catalogs.ActionCategory.Filtering
import com.harana.sdk.shared.models.flow.documentation.ActionDocumentation
import com.harana.sdk.shared.models.flow.parameters.ParameterGroup
import com.harana.sdk.shared.models.flow.utils.Id

import scala.reflect.runtime.universe.TypeTag

trait DropColumnsInfo extends Action1To1TypeInfo[DataFrameInfo, DataFrameInfo] with ActionDocumentation {

  val id: Id = "50f4a041-c710-4f1b-b3b2-d0a6bd976b61"
  val name = "Distinct"
  val since = Version(1, 2, 0)
  val category = Filtering

  lazy val portI_0: TypeTag[DataFrameInfo] = typeTag
  lazy val portO_0: TypeTag[DataFrameInfo] = typeTag

  override val parameterGroups = List.empty[ParameterGroup]

}

object DropColumnsInfo extends DropColumnsInfo