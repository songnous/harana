package com.harana.sdk.shared.models.flow.actions

import com.harana.sdk.shared.models.common.Version
import com.harana.sdk.shared.models.flow.Action3To1Info
import com.harana.sdk.shared.models.flow.catalogs.ActionCategory.Filtering
import com.harana.sdk.shared.models.flow.actionobjects.{DataFrameInfo, ProjectorInfo}
import com.harana.sdk.shared.models.flow.catalogs.ActionCategory.Filtering
import com.harana.sdk.shared.models.flow.documentation.ActionDocumentation
import com.harana.sdk.shared.models.flow.parameters.Parameter
import com.harana.sdk.shared.models.flow.utils.Id

import scala.reflect.runtime.universe.{TypeTag, typeTag}

trait DistinctInfo extends Action3To1Info[DataFrameInfo, DataFrameInfo, DataFrameInfo, DataFrameInfo] with ActionDocumentation {

  val id: Id = "53c98d28-10c7-46cb-8173-b7b6b9651afa"
  val name = "Distinct"
  val since = Version(1, 2, 0)
  val category = Filtering

  lazy val portI_0: TypeTag[DataFrameInfo] = typeTag
  lazy val portI_1: TypeTag[DataFrameInfo] = typeTag
  lazy val portI_2: TypeTag[DataFrameInfo] = typeTag
  lazy val portO_0: TypeTag[DataFrameInfo] = typeTag

  val parameters = Array.empty[Parameter[_]]

}

object DistinctInfo extends DistinctInfo {
  def apply(pos: (Int, Int), color: Option[String] = None) = new DistinctInfo {
    override val position = Some(pos)
    override val overrideColor = color
  }
}