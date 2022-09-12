package com.harana.sdk.shared.models.flow.actions

import com.harana.sdk.shared.models.common.Version
import com.harana.sdk.shared.models.flow.catalogs.ActionCategory.Transformation
import com.harana.sdk.shared.models.flow.actionobjects.SqlTransformerInfo
import com.harana.sdk.shared.models.flow.catalogs.ActionCategory.Transformation.Custom
import com.harana.sdk.shared.models.flow.documentation.ActionDocumentation
import com.harana.sdk.shared.models.flow.utils.Id

import scala.reflect.runtime.universe.{TypeTag, typeTag}

trait SqlTransformationInfo extends TransformerAsActionInfo[SqlTransformerInfo] with ActionDocumentation {

  val id: Id = "6cba4400-d966-4a2a-8356-b37f37b4c73f"
  val name = "SQL Transformation"
  val description = "Executes an SQL transformation on a DataFrame"
  val since = Version(0, 4, 0)
  val category = Custom

  lazy val portO_1: TypeTag[SqlTransformerInfo] = typeTag
}

object SqlTransformationInfo extends SqlTransformationInfo