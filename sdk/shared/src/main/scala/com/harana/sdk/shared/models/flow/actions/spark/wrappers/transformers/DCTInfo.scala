package com.harana.sdk.shared.models.flow.actions.spark.wrappers.transformers

import com.harana.sdk.shared.models.common.Version
import com.harana.sdk.shared.models.flow.catalogs.ActionCategory.Transformation
import com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.transformers.DiscreteCosineTransformerInfo
import com.harana.sdk.shared.models.flow.actions.TransformerAsActionInfo
import com.harana.sdk.shared.models.flow.catalogs.ActionCategory.Transformation.FeatureConversion
import com.harana.sdk.shared.models.flow.documentation.SparkActionDocumentation
import com.harana.sdk.shared.models.flow.utils.Id

import scala.reflect.runtime.universe.TypeTag

trait DCTInfo extends TransformerAsActionInfo[DiscreteCosineTransformerInfo] with SparkActionDocumentation {

  val id: Id = "68cd1492-501d-4c4f-9fde-f742d652111a"
  val name = "DCT"
  val since = Version(1,0,0)
  val docsGuideLocation = Some("ml-features.html#discrete-cosine-transform-dct")
  val category = FeatureConversion

  lazy val portO_1: TypeTag[DiscreteCosineTransformerInfo] = typeTag

}

object DCTInfo extends DCTInfo
