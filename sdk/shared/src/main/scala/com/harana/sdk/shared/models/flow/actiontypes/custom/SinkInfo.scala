package com.harana.sdk.shared.models.flow.actiontypes.custom

import com.harana.sdk.shared.models.common.Version
import com.harana.sdk.shared.models.flow.Action1To1TypeInfo
import com.harana.sdk.shared.models.flow.actionobjects.DataFrameInfo
import com.harana.sdk.shared.models.flow.catalogs.ActionCategory.IO
import com.harana.sdk.shared.models.flow.parameters.ParameterGroup
import izumi.reflect.Tag

import scala.reflect.runtime.{universe => ru}

trait SinkInfo extends Action1To1TypeInfo[DataFrameInfo, DataFrameInfo] {

  val id = "e652238f-7415-4da6-95c6-ee33808561b2"
  val name = "sink"
  val since = Version(1, 0, 0)
  override val parameterGroups = List.empty[ParameterGroup]
  val category = IO

  @transient
  lazy val portI_0: Tag[DataFrameInfo] = Tag[DataFrameInfo]

  @transient
  lazy val portO_0: Tag[DataFrameInfo] = Tag[DataFrameInfo]

}

object SinkInfo extends SinkInfo