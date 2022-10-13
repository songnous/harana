package com.harana.sdk.shared.models.flow.actiontypes

import com.harana.sdk.shared.models.common.Version
import com.harana.sdk.shared.models.flow.Action3To1TypeInfo
import com.harana.sdk.shared.models.flow.actionobjects.DataFrameInfo
import com.harana.sdk.shared.models.flow.catalogs.ActionCategory.Filtering
import com.harana.sdk.shared.models.flow.documentation.ActionDocumentation
import com.harana.sdk.shared.models.flow.parameters.{ParameterGroup, StringParameter}
import com.harana.sdk.shared.models.flow.utils.Id
import izumi.reflect.Tag

import izumi.reflect.Tag

trait DistinctInfo extends Action3To1TypeInfo[DataFrameInfo, DataFrameInfo, DataFrameInfo, DataFrameInfo] with ActionDocumentation {

  val id: Id = "53c98d28-10c7-46cb-8173-b7b6b9651afa"
  val name = "Distinct"
  val since = Version(1, 2, 0)
  val category = Filtering

  lazy val portI_0: Tag[DataFrameInfo] = typeTag
  lazy val portI_1: Tag[DataFrameInfo] = typeTag
  lazy val portI_2: Tag[DataFrameInfo] = typeTag
  lazy val portO_0: Tag[DataFrameInfo] = typeTag

  val sParameter = StringParameter("test")

  override val parameterGroups = List(ParameterGroup("", sParameter))

  //override val parameterGroups = List.empty[ParameterGroup]

}

object DistinctInfo extends DistinctInfo