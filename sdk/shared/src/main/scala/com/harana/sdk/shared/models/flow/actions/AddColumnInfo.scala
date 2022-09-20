package com.harana.sdk.shared.models.flow.actions

import com.harana.sdk.shared.models.common.Version
import com.harana.sdk.shared.models.flow.Action3To1Info
import com.harana.sdk.shared.models.flow.catalogs.ActionCategory.Filtering
import com.harana.sdk.shared.models.flow.actionobjects.ProjectorInfo
import com.harana.sdk.shared.models.flow.actions.dataframe.DataFrameInfo
import com.harana.sdk.shared.models.flow.catalogs.ActionCategory.Filtering
import com.harana.sdk.shared.models.flow.documentation.ActionDocumentation
import com.harana.sdk.shared.models.flow.parameters.Parameter
import com.harana.sdk.shared.models.flow.utils.Id

import scala.reflect.runtime.universe.{TypeTag, typeTag}

trait AddColumnInfo extends Action3To1Info[DataFrameInfo, DataFrameInfo, DataFrameInfo, DataFrameInfo] with ActionDocumentation {

  val id: Id = "7e231c74-ab39-4fd3-9e48-4ab7acb6ad09"
  val name = "SQL Query"
  val since = Version(1, 2, 0)
  val category = Filtering

  lazy val portI_0: TypeTag[DataFrameInfo] = typeTag
  lazy val portI_1: TypeTag[DataFrameInfo] = typeTag
  lazy val portI_2: TypeTag[DataFrameInfo] = typeTag

  lazy val portO_0: TypeTag[DataFrameInfo] = typeTag

  val parameters = Array.empty[Parameter[_]]

}

object AddColumnInfo extends AddColumnInfo