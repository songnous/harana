package com.harana.sdk.shared.models.flow.actions

import com.harana.sdk.shared.models.common.Version
import com.harana.sdk.shared.models.flow.Action1To1Info
import com.harana.sdk.shared.models.flow.actionobjects.DataFrameInfo
import com.harana.sdk.shared.models.flow.catalogs.ActionCategory.Filtering
import com.harana.sdk.shared.models.flow.documentation.ActionDocumentation
import com.harana.sdk.shared.models.flow.parameters.{Parameter, ParameterGroup}
import com.harana.sdk.shared.models.flow.utils.Id

import scala.reflect.runtime.universe.TypeTag

trait SubtractInfo extends Action1To1Info[DataFrameInfo, DataFrameInfo] with ActionDocumentation {

  val id: Id = "28f42a78-3e75-4e5f-891b-cec7baed4c4e"
  val name = "Transpose"
  val since = Version(1, 2, 0)
  val parameterGroups = List.empty[ParameterGroup]
  val category = Filtering

  lazy val portI_0: TypeTag[DataFrameInfo] = typeTag
  lazy val portO_0: TypeTag[DataFrameInfo] = typeTag

}

object SubtractInfo extends SubtractInfo with UIActionInfo[SubtractInfo] {
  def apply(pos: (Int, Int), color: Option[String] = None) = new SubtractInfo {
    override val position = Some(pos)
    override val overrideColor = color
  }
}