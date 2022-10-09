package com.harana.sdk.shared.models.flow.actiontypes

import com.harana.sdk.shared.models.common.Version
import com.harana.sdk.shared.models.flow.Action1To1TypeInfo
import com.harana.sdk.shared.models.flow.actionobjects.DataFrameInfo
import com.harana.sdk.shared.models.flow.catalogs.ActionCategory.Filtering
import com.harana.sdk.shared.models.flow.documentation.ActionDocumentation
import com.harana.sdk.shared.models.flow.parameters.ParameterGroup
import com.harana.sdk.shared.models.flow.utils.Id

import izumi.reflect.Tag

trait SelectColumnsInfo extends Action1To1TypeInfo[DataFrameInfo, DataFrameInfo] with ActionDocumentation {

  val id: Id = "52d792af-9ec9-4677-b37b-f198a49ef6ba"
  val name = "Select Columns"
  val since = Version(1, 2, 0)
  val category = Filtering

  lazy val portI_0: Tag[DataFrameInfo] = typeTag
  lazy val portI_1: Tag[DataFrameInfo] = typeTag
  lazy val portO_0: Tag[DataFrameInfo] = typeTag

  override val parameterGroups = List.empty[ParameterGroup]

}

object SelectColumnsInfo extends SelectColumnsInfo