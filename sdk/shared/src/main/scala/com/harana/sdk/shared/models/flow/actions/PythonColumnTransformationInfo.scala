package com.harana.sdk.shared.models.flow.actions

import com.harana.sdk.shared.models.common.Version
import com.harana.sdk.shared.models.flow.catalogs.ActionCategory.Transformation
import com.harana.sdk.shared.models.flow.actionobjects.PythonColumnTransformerInfo
import com.harana.sdk.shared.models.flow.catalogs.ActionCategory.Transformation.Custom
import com.harana.sdk.shared.models.flow.documentation.ActionDocumentation
import com.harana.sdk.shared.models.flow.utils.Id

import scala.reflect.runtime.universe.TypeTag

trait PythonColumnTransformationInfo extends TransformerAsActionInfo[PythonColumnTransformerInfo] with ActionDocumentation {

  val id: Id = "9951d301-7eb7-473b-81ad-0f8659619784"
  val name = "Python Column Transformation"
  val description = "Executes a custom Python transformation on a column of a DataFrame"
  val since = Version(1, 3, 0)
  val category = Custom

  lazy val portO_1: TypeTag[PythonColumnTransformerInfo] = typeTag

}

object PythonColumnTransformationInfo extends PythonColumnTransformationInfo