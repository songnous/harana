package com.harana.sdk.shared.models.flow.actiontypes

import com.harana.sdk.shared.models.common.Version
import com.harana.sdk.shared.models.flow.actionobjects.PythonTransformerInfo
import com.harana.sdk.shared.models.flow.catalogs.ActionCategory.Transformation.Custom
import com.harana.sdk.shared.models.flow.documentation.ActionDocumentation
import com.harana.sdk.shared.models.flow.utils.Id

import izumi.reflect.Tag

trait PythonTransformationInfo extends TransformerAsActionInfo[PythonTransformerInfo] with ActionDocumentation {

  val id: Id = "a721fe2a-5d7f-44b3-a1e7-aade16252ead"
  val name = "python-transformation"
  val since = Version(1, 3, 0)
  val category = Custom

  lazy val portO_1: Tag[PythonTransformerInfo] = typeTag

}

object PythonTransformationInfo extends PythonTransformationInfo