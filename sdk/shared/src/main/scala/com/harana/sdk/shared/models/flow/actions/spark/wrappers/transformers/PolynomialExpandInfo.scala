package com.harana.sdk.shared.models.flow.actions.spark.wrappers.transformers

import com.harana.sdk.shared.models.common.Version
import com.harana.sdk.shared.models.flow.catalogs.ActionCategory.Transformation
import com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.transformers.PolynomialExpanderInfo
import com.harana.sdk.shared.models.flow.actions.TransformerAsActionInfo
import com.harana.sdk.shared.models.flow.catalogs.ActionCategory.Transformation.FeatureConversion
import com.harana.sdk.shared.models.flow.documentation.SparkActionDocumentation
import com.harana.sdk.shared.models.flow.utils.Id

import scala.reflect.runtime.universe.TypeTag

trait PolynomialExpandInfo extends TransformerAsActionInfo[PolynomialExpanderInfo] with SparkActionDocumentation {

  val id: Id = "4a741088-3180-4373-940d-741b2f1620de"
  val name = "Polynomial Expansion"
  val since = Version(1,0,0)
  val docsGuideLocation = Some("ml-features.html#polynomialexpansion")
  val category = FeatureConversion

  lazy val portO_1: TypeTag[PolynomialExpanderInfo] = typeTag

}

object PolynomialExpandInfo extends PolynomialExpandInfo {
  def apply(pos: (Int, Int), color: Option[String] = None) = new PolynomialExpandInfo {
    override val position = Some(pos)
    override val overrideColor = color
  }
}