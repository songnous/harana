package com.harana.sdk.shared.models.flow.actions.spark.wrappers.transformers

import com.harana.sdk.shared.models.common.Version
import com.harana.sdk.shared.models.flow.catalogs.ActionCategory.Transformation
import com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.transformers.HashingTFTransformerInfo
import com.harana.sdk.shared.models.flow.actions.{TransformerAsActionInfo, UIActionInfo}
import com.harana.sdk.shared.models.flow.catalogs.ActionCategory.Transformation.TextProcessing
import com.harana.sdk.shared.models.flow.documentation.SparkActionDocumentation
import com.harana.sdk.shared.models.flow.utils.Id

import scala.reflect.runtime.universe.TypeTag

trait HashingTFInfo extends TransformerAsActionInfo[HashingTFTransformerInfo] with SparkActionDocumentation {

  val id: Id = "4266c9c0-6863-44ca-967b-62927ca34434"
  val name = "HashingTF"
  val since = Version(1,0,0)
  val docsGuideLocation = Some("ml-features.html#tf-idf")
  val category = TextProcessing

  lazy val portO_1: TypeTag[HashingTFTransformerInfo] = typeTag

}

object HashingTFInfo extends HashingTFInfo with UIActionInfo[HashingTFInfo] {
  def apply(pos: (Int, Int), color: Option[String] = None) = new HashingTFInfo {
    override val position = Some(pos)
    override val overrideColor = color
  }
}