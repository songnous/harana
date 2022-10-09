package com.harana.sdk.shared.models.flow.actiontypes

import com.harana.sdk.shared.models.common.Version
import com.harana.sdk.shared.models.flow.actionobjects.TypeConverterInfo
import com.harana.sdk.shared.models.flow.catalogs.ActionCategory.Transformation.FeatureConversion
import com.harana.sdk.shared.models.flow.documentation.ActionDocumentation

import izumi.reflect.Tag

trait ConvertTypeInfo extends TransformerAsActionInfo[TypeConverterInfo] with ActionDocumentation {

  val id = "04084863-fdda-46fd-b1fe-796c6b5a0967"
  val name = "convert-type"
  val since = Version(0, 4, 0)
  val category = FeatureConversion

  lazy val portO_1: Tag[TypeConverterInfo] = typeTag
}

object ConvertTypeInfo extends ConvertTypeInfo