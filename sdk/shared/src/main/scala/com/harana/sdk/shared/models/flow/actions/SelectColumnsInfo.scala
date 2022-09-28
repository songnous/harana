package com.harana.sdk.shared.models.flow.actions

import com.harana.sdk.shared.models.common.Version
import com.harana.sdk.shared.models.flow.actionobjects.DataFrameInfo
import com.harana.sdk.shared.models.flow.{Action1To1Info, Action2To1Info}
import com.harana.sdk.shared.models.flow.catalogs.ActionCategory.Filtering
import com.harana.sdk.shared.models.flow.documentation.ActionDocumentation
import com.harana.sdk.shared.models.flow.parameters.{Parameter, ParameterGroup}
import com.harana.sdk.shared.models.flow.utils.Id

import scala.reflect.runtime.universe.TypeTag

trait SelectColumnsInfo extends Action1To1Info[DataFrameInfo, DataFrameInfo] with ActionDocumentation {

  val id: Id = "52d792af-9ec9-4677-b37b-f198a49ef6ba"
  val name = "Select Columns"
  val since = Version(1, 2, 0)
  val category = Filtering

  lazy val portI_0: TypeTag[DataFrameInfo] = typeTag
  lazy val portI_1: TypeTag[DataFrameInfo] = typeTag
  lazy val portO_0: TypeTag[DataFrameInfo] = typeTag

  val parameterGroups = List.empty[ParameterGroup]

}

object SelectColumnsInfo extends SelectColumnsInfo with UIActionInfo[SelectColumnsInfo] {
  def apply(pos: (Int, Int), color: Option[String] = None) = new SelectColumnsInfo {
    override val position = Some(pos)
    override val overrideColor = color
  }
}