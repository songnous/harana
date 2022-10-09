package com.harana.sdk.shared.models.flow.actiontypes

import com.harana.sdk.shared.models.common.Version
import com.harana.sdk.shared.models.flow.actionobjects.DatetimeDecomposerInfo
import com.harana.sdk.shared.models.flow.catalogs.ActionCategory.Transformation.FeatureConversion
import com.harana.sdk.shared.models.flow.documentation.ActionDocumentation
import com.harana.sdk.shared.models.flow.utils.Id

import izumi.reflect.Tag

trait DecomposeDatetimeInfo extends TransformerAsActionInfo[DatetimeDecomposerInfo] with ActionDocumentation {

  val id: Id = "6c18b05e-7db7-4315-bce1-3291ed530675"
  val name = "decompose-datetime"
  val since = Version(0, 4, 0)
  val category = FeatureConversion

  lazy val portO_1: Tag[DatetimeDecomposerInfo] = typeTag

}

object DecomposeDatetimeInfo extends DecomposeDatetimeInfo