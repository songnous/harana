package com.harana.sdk.shared.models.flow.actiontypes

import com.harana.sdk.shared.models.common.Version
import com.harana.sdk.shared.models.flow.actionobjects.DataFrameInfo
import com.harana.sdk.shared.models.flow.catalogs.ActionCategory.SetAction
import com.harana.sdk.shared.models.flow.documentation.ActionDocumentation
import com.harana.sdk.shared.models.flow.parameters.{ParameterGroup, Parameters}
import com.harana.sdk.shared.models.flow.utils.Id
import com.harana.sdk.shared.models.flow.{Action2To1TypeInfo, PortPosition}
import izumi.reflect.Tag

import scala.reflect.runtime.{universe => ru}

trait UnionInfo extends Action2To1TypeInfo[DataFrameInfo, DataFrameInfo, DataFrameInfo]
  with Parameters
  with ActionDocumentation {

  val id: Id = "90fed07b-d0a9-49fd-ae23-dd7000a1d8ad"
  val name = "union"
  val since = Version(0, 4, 0)
  override val parameterGroups = List.empty[ParameterGroup]
  val category = SetAction

  override val inputPortsLayout = List(PortPosition.Left, PortPosition.Right)

  @transient
  lazy val portI_0: Tag[DataFrameInfo] = Tag[DataFrameInfo]

  @transient
  lazy val portI_1: Tag[DataFrameInfo] = Tag[DataFrameInfo]

  @transient
  lazy val portO_0: Tag[DataFrameInfo] = Tag[DataFrameInfo]

}

object UnionInfo extends UnionInfo