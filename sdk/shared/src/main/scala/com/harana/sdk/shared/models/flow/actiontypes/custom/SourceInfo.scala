package com.harana.sdk.shared.models.flow.actiontypes.custom

import com.harana.sdk.shared.models.common.Version
import com.harana.sdk.shared.models.flow.Action0To1TypeInfo
import com.harana.sdk.shared.models.flow.actionobjects.DataFrameInfo
import com.harana.sdk.shared.models.flow.catalogs.ActionCategory.IO
import com.harana.sdk.shared.models.flow.parameters.ParameterGroup
import izumi.reflect.Tag

import scala.reflect.runtime.{universe => ru}

trait SourceInfo extends Action0To1TypeInfo[DataFrameInfo] {

  val id = "f94b04d7-ec34-42f7-8100-93fe235c89f8"
  val name = "source"
  val since = Version(1, 0, 0)
  override val parameterGroups = List.empty[ParameterGroup]
  val category = IO

  @transient
  lazy val portO_0: Tag[DataFrameInfo] = Tag[DataFrameInfo]

}

object SourceInfo extends SourceInfo