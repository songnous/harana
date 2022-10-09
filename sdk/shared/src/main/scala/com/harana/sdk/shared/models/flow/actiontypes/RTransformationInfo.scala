package com.harana.sdk.shared.models.flow.actiontypes

import com.harana.sdk.shared.models.common.Version
import com.harana.sdk.shared.models.flow.actionobjects.RTransformerInfo
import com.harana.sdk.shared.models.flow.catalogs.ActionCategory.Transformation.Custom
import com.harana.sdk.shared.models.flow.documentation.ActionDocumentation
import com.harana.sdk.shared.models.flow.utils.Id

import izumi.reflect.Tag

trait RTransformationInfo extends TransformerAsActionInfo[RTransformerInfo] with ActionDocumentation {

  val id: Id = "b578ad31-3a5b-4b94-a8d1-4c319fac6add"
  val name = "r-transformation"
  val since = Version(1, 3, 0)
  val category = Custom

  lazy val portO_1: Tag[RTransformerInfo] = typeTag

}

object RTransformationInfo extends RTransformationInfo