package com.harana.sdk.shared.models.flow.actions

import com.harana.sdk.shared.models.common.Version
import com.harana.sdk.shared.models.flow.catalogs.ActionCategory.Transformation
import com.harana.sdk.shared.models.flow.actionobjects.PythonTransformerInfo
import com.harana.sdk.shared.models.flow.catalogs.ActionCategory.Transformation.Custom
import com.harana.sdk.shared.models.flow.documentation.ActionDocumentation
import com.harana.sdk.shared.models.flow.utils.Id

import scala.reflect.runtime.universe.TypeTag

trait PythonTransformationInfo extends TransformerAsActionInfo[PythonTransformerInfo] with ActionDocumentation {

  val id: Id = "a721fe2a-5d7f-44b3-a1e7-aade16252ead"
  val name = "Python Transformation"
  val since = Version(1, 3, 0)
  val category = Custom

  lazy val portO_1: TypeTag[PythonTransformerInfo] = typeTag

}

object PythonTransformationInfo extends PythonTransformationInfo