package com.harana.sdk.shared.models.flow.actiontypes.spark.wrappers.transformers

import com.harana.sdk.shared.models.common.Version
import com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.transformers.HashingTFTransformerInfo
import com.harana.sdk.shared.models.flow.actiontypes.TransformerAsActionInfo
import com.harana.sdk.shared.models.flow.catalogs.ActionCategory.Transformation.TextProcessing
import com.harana.sdk.shared.models.flow.documentation.SparkActionDocumentation
import com.harana.sdk.shared.models.flow.utils.Id

import izumi.reflect.Tag

trait HashingTFInfo extends TransformerAsActionInfo[HashingTFTransformerInfo] with SparkActionDocumentation {

  val id: Id = "4266c9c0-6863-44ca-967b-62927ca34434"
  val name = "hashing-tf"
  val since = Version(1,0,0)
  val docsGuideLocation = Some("ml-features.html#tf-idf")
  val category = TextProcessing

  lazy val portO_1: Tag[HashingTFTransformerInfo] = typeTag

}

object HashingTFInfo extends HashingTFInfo