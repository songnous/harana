package com.harana.sdk.shared.models.flow.actiontypes.spark.wrappers.transformers

import com.harana.sdk.shared.models.common.Version
import com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.transformers.DiscreteCosineTransformerInfo
import com.harana.sdk.shared.models.flow.actiontypes.TransformerAsActionInfo
import com.harana.sdk.shared.models.flow.catalogs.ActionCategory.Transformation.FeatureConversion
import com.harana.sdk.shared.models.flow.documentation.SparkActionDocumentation
import com.harana.sdk.shared.models.flow.utils.Id

import izumi.reflect.Tag

trait DCTInfo extends TransformerAsActionInfo[DiscreteCosineTransformerInfo] with SparkActionDocumentation {

  val id: Id = "68cd1492-501d-4c4f-9fde-f742d652111a"
  val name = "dct"
  val since = Version(1,0,0)
  val docsGuideLocation = Some("ml-features.html#discrete-cosine-transform-dct")
  val category = FeatureConversion

  lazy val portO_1: Tag[DiscreteCosineTransformerInfo] = typeTag

}

object DCTInfo extends DCTInfo