package com.harana.sdk.shared.models.flow.actiontypes

import com.harana.sdk.shared.models.common.Version
import com.harana.sdk.shared.models.flow.actionobjects.DatetimeComposerInfo
import com.harana.sdk.shared.models.flow.catalogs.ActionCategory.Transformation.FeatureConversion
import com.harana.sdk.shared.models.flow.documentation.ActionDocumentation
import com.harana.sdk.shared.models.flow.utils.Id
import izumi.reflect.Tag

import izumi.reflect.Tag

trait ComposeDatetimeInfo extends TransformerAsActionInfo[DatetimeComposerInfo] with ActionDocumentation {

  val id: Id = "291cdd16-b57a-4613-abbe-3fd73011e579"
  val name = "compose-datetime"
  val since = Version(1, 3, 0)
  val category = FeatureConversion

  lazy val portO_1: Tag[DatetimeComposerInfo] = typeTag

}

object ComposeDatetimeInfo extends ComposeDatetimeInfo