package com.harana.sdk.shared.models.flow.actions.spark.wrappers.transformers

import com.harana.sdk.shared.models.common.Version
import com.harana.sdk.shared.models.flow.catalogs.ActionCategory.Transformation
import com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.transformers.NGramTransformerInfo
import com.harana.sdk.shared.models.flow.actions.{TransformerAsActionInfo, UIActionInfo}
import com.harana.sdk.shared.models.flow.catalogs.ActionCategory.Transformation.TextProcessing
import com.harana.sdk.shared.models.flow.documentation.SparkActionDocumentation
import com.harana.sdk.shared.models.flow.utils.Id

import scala.reflect.runtime.universe.TypeTag

trait ConvertToNGramsInfo extends TransformerAsActionInfo[NGramTransformerInfo] with SparkActionDocumentation {

  val id: Id = "06a73bfe-4e1a-4cde-ae6c-ad5a31f72496"
  val name = "Convert To n-grams"
  val since = Version(1,0,0)
  val docsGuideLocation = Some("ml-features.html#n-gram")
  val category = TextProcessing

  lazy val portO_1: TypeTag[NGramTransformerInfo] = typeTag

}

object ConvertToNGramsInfo extends ConvertToNGramsInfo with UIActionInfo[ConvertToNGramsInfo] {
  def apply(pos: (Int, Int), color: Option[String] = None) = new ConvertToNGramsInfo {
    override val position = Some(pos)
    override val overrideColor = color
  }
}