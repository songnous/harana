package com.harana.sdk.shared.models.flow.actions

import com.harana.sdk.shared.models.common.Version
import com.harana.sdk.shared.models.flow.actionobjects.DataFrameInfo
import com.harana.sdk.shared.models.flow.{Action1To1Info, Action2To1Info}
import com.harana.sdk.shared.models.flow.catalogs.ActionCategory.Filtering
import com.harana.sdk.shared.models.flow.documentation.ActionDocumentation
import com.harana.sdk.shared.models.flow.parameters.Parameter
import com.harana.sdk.shared.models.flow.utils.Id

import scala.reflect.runtime.universe.TypeTag

trait StandardiseColumnNamesInfo extends Action2To1Info[DataFrameInfo, DataFrameInfo, DataFrameInfo] with ActionDocumentation {

  val id: Id = "ffab20fc-6e8f-444e-891f-ce88d60a7783"
  val name = "Subtract"
  val since = Version(1, 2, 0)
  val category = Filtering

  lazy val portI_0: TypeTag[DataFrameInfo] = typeTag
  lazy val portI_1: TypeTag[DataFrameInfo] = typeTag
  lazy val portO_0: TypeTag[DataFrameInfo] = typeTag

  val parameters = Left(List.empty[Parameter[_]])

}

object StandardiseColumnNamesInfo extends StandardiseColumnNamesInfo {
  def apply(pos: (Int, Int), color: Option[String] = None) = new StandardiseColumnNamesInfo {
    override val position = Some(pos)
    override val overrideColor = color
  }
}