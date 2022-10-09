package com.harana.sdk.shared.models.flow.actiontypes

import com.harana.sdk.shared.models.common.Version
import com.harana.sdk.shared.models.flow.actionobjects.PythonColumnTransformerInfo
import com.harana.sdk.shared.models.flow.catalogs.ActionCategory.Transformation.Custom
import com.harana.sdk.shared.models.flow.documentation.ActionDocumentation
import com.harana.sdk.shared.models.flow.utils.Id

import izumi.reflect.Tag

trait PythonColumnTransformationInfo extends TransformerAsActionInfo[PythonColumnTransformerInfo] with ActionDocumentation {

  val id: Id = "9951d301-7eb7-473b-81ad-0f8659619784"
  val name = "python-column-transformation"
  val since = Version(1, 3, 0)
  val category = Custom

  lazy val portO_1: Tag[PythonColumnTransformerInfo] = typeTag

}

object PythonColumnTransformationInfo extends PythonColumnTransformationInfo