package com.harana.sdk.shared.models.flow.actions.spark.wrappers.transformers

import com.harana.sdk.shared.models.common.Version
import com.harana.sdk.shared.models.flow.catalogs.ActionCategory.Transformation
import com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.transformers.NormalizerInfo
import com.harana.sdk.shared.models.flow.actions.TransformerAsActionInfo
import com.harana.sdk.shared.models.flow.catalogs.ActionCategory.Transformation.FeatureConversion
import com.harana.sdk.shared.models.flow.documentation.SparkActionDocumentation
import com.harana.sdk.shared.models.flow.utils.Id

import scala.reflect.runtime.universe.TypeTag

trait NormalizeInfo extends TransformerAsActionInfo[NormalizerInfo] with SparkActionDocumentation {

  val id: Id = "20f3d9ef-9b04-49c6-8acd-7ddafdedcb39"
  val name = "Normalize"
  val since = Version(1,0,0)
  val docsGuideLocation = Some("ml-features.html#normalizer")
  val category = FeatureConversion

  lazy val portO_1: TypeTag[NormalizerInfo] = typeTag

}

object NormalizeInfo extends NormalizeInfo {
  def apply() = new NormalizeInfo {}
}