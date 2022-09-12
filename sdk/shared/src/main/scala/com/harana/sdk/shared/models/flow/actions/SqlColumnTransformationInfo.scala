package com.harana.sdk.shared.models.flow.actions

import com.harana.sdk.shared.models.common.Version
import com.harana.sdk.shared.models.flow.catalogs.ActionCategory.Transformation
import com.harana.sdk.shared.models.flow.actionobjects.SqlColumnTransformerInfo
import com.harana.sdk.shared.models.flow.catalogs.ActionCategory.Transformation.Custom
import com.harana.sdk.shared.models.flow.documentation.ActionDocumentation
import com.harana.sdk.shared.models.flow.utils.Id

import scala.reflect.runtime.universe.TypeTag

trait SqlColumnTransformationInfo extends TransformerAsActionInfo[SqlColumnTransformerInfo] with ActionDocumentation {

  val id: Id = "012876d9-7a72-47f9-98e4-8ed26db14d6d"
  val name = "SQL Column Transformation"
  val description = "Executes a SQL transformation on a column of a DataFrame"
  val since = Version(1, 1, 0)
  val category = Custom

  lazy val portO_1: TypeTag[SqlColumnTransformerInfo] = typeTag

}

object SqlColumnTransformationInfo extends SqlColumnTransformationInfo