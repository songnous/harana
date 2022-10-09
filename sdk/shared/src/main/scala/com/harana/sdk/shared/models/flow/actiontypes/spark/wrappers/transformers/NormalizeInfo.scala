package com.harana.sdk.shared.models.flow.actiontypes.spark.wrappers.transformers

import com.harana.sdk.shared.models.common.Version
import com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.transformers.NormalizerInfo
import com.harana.sdk.shared.models.flow.actiontypes.TransformerAsActionInfo
import com.harana.sdk.shared.models.flow.catalogs.ActionCategory.Transformation.FeatureConversion
import com.harana.sdk.shared.models.flow.documentation.SparkActionDocumentation
import com.harana.sdk.shared.models.flow.utils.Id

import izumi.reflect.Tag

trait NormalizeInfo extends TransformerAsActionInfo[NormalizerInfo] with SparkActionDocumentation {

  val id: Id = "20f3d9ef-9b04-49c6-8acd-7ddafdedcb39"
  val name = "normalize"
  val since = Version(1,0,0)
  val docsGuideLocation = Some("ml-features.html#normalizer")
  val category = FeatureConversion

  lazy val portO_1: Tag[NormalizerInfo] = typeTag

}

object NormalizeInfo extends NormalizeInfo