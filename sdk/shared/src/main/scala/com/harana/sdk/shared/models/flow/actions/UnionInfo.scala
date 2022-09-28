package com.harana.sdk.shared.models.flow.actions

import com.harana.sdk.shared.models.common.Version
import com.harana.sdk.shared.models.flow.actionobjects.DataFrameInfo
import com.harana.sdk.shared.models.flow.catalogs.ActionCategory.SetAction
import com.harana.sdk.shared.models.flow.{Action2To1Info, PortPosition}
import com.harana.sdk.shared.models.flow.catalogs.ActionCategory.SetAction
import com.harana.sdk.shared.models.flow.documentation.ActionDocumentation
import com.harana.sdk.shared.models.flow.parameters.{Parameter, ParameterGroup, Parameters}
import com.harana.sdk.shared.models.flow.utils.Id

import scala.reflect.runtime.{universe => ru}

trait UnionInfo extends Action2To1Info[DataFrameInfo, DataFrameInfo, DataFrameInfo]
  with Parameters
  with ActionDocumentation {

  val id: Id = "90fed07b-d0a9-49fd-ae23-dd7000a1d8ad"
  val name = "Union"
  val since = Version(0, 4, 0)
  val parameterGroups = List.empty[ParameterGroup]
  val category = SetAction

  override val inputPortsLayout = List(PortPosition.Left, PortPosition.Right)

  @transient
  lazy val portI_0: ru.TypeTag[DataFrameInfo] = ru.typeTag[DataFrameInfo]

  @transient
  lazy val portI_1: ru.TypeTag[DataFrameInfo] = ru.typeTag[DataFrameInfo]

  @transient
  lazy val portO_0: ru.TypeTag[DataFrameInfo] = ru.typeTag[DataFrameInfo]

}

object UnionInfo extends UnionInfo with UIActionInfo[UnionInfo] {
  def apply(pos: (Int, Int), color: Option[String] = None) = new UnionInfo {
    override val position = Some(pos)
    override val overrideColor = color
  }
}