package com.harana.sdk.shared.models.flow.actiontypes

import com.harana.sdk.shared.models.common.Version
import com.harana.sdk.shared.models.flow.actionobjects.GetFromVectorTransformerInfo
import com.harana.sdk.shared.models.flow.catalogs.ActionCategory.Transformation.FeatureConversion
import com.harana.sdk.shared.models.flow.documentation.ActionDocumentation
import com.harana.sdk.shared.models.flow.utils.Id

import izumi.reflect.Tag

trait GetFromVectorInfo extends TransformerAsActionInfo[GetFromVectorTransformerInfo] with ActionDocumentation {

  val id: Id = "241a23d1-97a0-41d0-bcf7-5c2ccb24e3d5"
  val name = "get-from-vector"
  val since = Version(1, 2, 0)
  val category = FeatureConversion

  lazy val portO_1: Tag[GetFromVectorTransformerInfo] = typeTag

}

object GetFromVectorInfo extends GetFromVectorInfo