package com.harana.sdk.shared.models.flow.actions

import com.harana.sdk.shared.models.common.Version
import com.harana.sdk.shared.models.flow.Action3To1Info
import com.harana.sdk.shared.models.flow.catalogs.ActionCategory.Filtering
import com.harana.sdk.shared.models.flow.actionobjects.{DataFrameInfo, ProjectorInfo}
import com.harana.sdk.shared.models.flow.catalogs.ActionCategory.Filtering
import com.harana.sdk.shared.models.flow.documentation.ActionDocumentation
import com.harana.sdk.shared.models.flow.parameters.{Parameter, ParameterGroup}
import com.harana.sdk.shared.models.flow.utils.Id

import scala.reflect.runtime.universe.{TypeTag, typeTag}

trait DeduplicateInfo extends Action3To1Info[DataFrameInfo, DataFrameInfo, DataFrameInfo, DataFrameInfo] with ActionDocumentation {

  val id: Id = "7e231c74-ab39-4fd3-9e48-4ab7acb6ad09"
  val name = "SQL Query"
  val since = Version(1, 2, 0)
  val category = Filtering

  lazy val portI_0: TypeTag[DataFrameInfo] = typeTag
  lazy val portI_1: TypeTag[DataFrameInfo] = typeTag
  lazy val portI_2: TypeTag[DataFrameInfo] = typeTag

  lazy val portO_0: TypeTag[DataFrameInfo] = typeTag

  val parameterGroups = List.empty[ParameterGroup]

}

object DeduplicateInfo extends DeduplicateInfo with UIActionInfo[DeduplicateInfo] {
  def apply(pos: (Int, Int), color: Option[String] = None) = new DeduplicateInfo {
    override val position = Some(pos)
    override val overrideColor = color
  }
}