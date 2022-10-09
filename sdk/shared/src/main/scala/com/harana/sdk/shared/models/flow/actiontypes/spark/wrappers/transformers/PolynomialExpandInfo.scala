package com.harana.sdk.shared.models.flow.actiontypes.spark.wrappers.transformers

import com.harana.sdk.shared.models.common.Version
import com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.transformers.PolynomialExpanderInfo
import com.harana.sdk.shared.models.flow.actiontypes.TransformerAsActionInfo
import com.harana.sdk.shared.models.flow.catalogs.ActionCategory.Transformation.FeatureConversion
import com.harana.sdk.shared.models.flow.documentation.SparkActionDocumentation
import com.harana.sdk.shared.models.flow.utils.Id

import izumi.reflect.Tag

trait PolynomialExpandInfo extends TransformerAsActionInfo[PolynomialExpanderInfo] with SparkActionDocumentation {

  val id: Id = "4a741088-3180-4373-940d-741b2f1620de"
  val name = "polynomial-expansion"
  val since = Version(1,0,0)
  val docsGuideLocation = Some("ml-features.html#polynomialexpansion")
  val category = FeatureConversion

  lazy val portO_1: Tag[PolynomialExpanderInfo] = typeTag

}

object PolynomialExpandInfo extends PolynomialExpandInfo