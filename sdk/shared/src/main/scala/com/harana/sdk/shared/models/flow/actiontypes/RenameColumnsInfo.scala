package com.harana.sdk.shared.models.flow.actiontypes

import com.harana.sdk.shared.models.common.Version
import com.harana.sdk.shared.models.flow.actionobjects.DataFrameInfo
import com.harana.sdk.shared.models.flow.{Action1To1TypeInfo, Action2To1TypeInfo}
import com.harana.sdk.shared.models.flow.catalogs.ActionCategory.Filtering
import com.harana.sdk.shared.models.flow.documentation.ActionDocumentation
import com.harana.sdk.shared.models.flow.parameters.{Parameter, ParameterGroup}
import com.harana.sdk.shared.models.flow.utils.Id
import shapeless.HMap

import scala.reflect.runtime.universe.TypeTag

trait RenameColumnsInfo extends Action1To1TypeInfo[DataFrameInfo, DataFrameInfo] with ActionDocumentation {

  val id: Id = "ffab20fc-6e8f-444e-891f-ce88d60a7783"
  val name = "Rename Columns"
  val since = Version(1, 2, 0)
  val category = Filtering

  lazy val portI_0: TypeTag[DataFrameInfo] = typeTag
  lazy val portO_0: TypeTag[DataFrameInfo] = typeTag

  override val parameterGroups = List.empty[ParameterGroup]

}

object RenameColumnsInfo extends RenameColumnsInfo