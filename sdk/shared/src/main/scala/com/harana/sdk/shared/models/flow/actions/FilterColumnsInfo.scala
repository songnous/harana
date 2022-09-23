package com.harana.sdk.shared.models.flow.actions

import com.harana.sdk.shared.models.common.Version
import com.harana.sdk.shared.models.flow.catalogs.ActionCategory.{Filtering, SetAction}
import com.harana.sdk.shared.models.flow.actionobjects.ColumnsFiltererInfo
import com.harana.sdk.shared.models.flow.catalogs.ActionCategory.Filtering
import com.harana.sdk.shared.models.flow.documentation.ActionDocumentation
import com.harana.sdk.shared.models.flow.utils.Id

import scala.reflect.runtime.universe.{TypeTag, typeTag}

trait FilterColumnsInfo extends TransformerAsActionInfo[ColumnsFiltererInfo] with ActionDocumentation {

  val id: Id = "6534f3f4-fa3a-49d9-b911-c213d3da8b5d"
  val name = "Filter Columns"
  val since = Version(1, 0, 0)
  val category = Filtering

  lazy val portO_1: TypeTag[ColumnsFiltererInfo] = typeTag

}

object FilterColumnsInfo extends FilterColumnsInfo {
  def apply() = new FilterColumnsInfo {}
}