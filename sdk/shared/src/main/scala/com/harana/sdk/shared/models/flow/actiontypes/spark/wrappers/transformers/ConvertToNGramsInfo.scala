package com.harana.sdk.shared.models.flow.actiontypes.spark.wrappers.transformers

import com.harana.sdk.shared.models.common.Version
import com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.transformers.NGramTransformerInfo
import com.harana.sdk.shared.models.flow.actiontypes.TransformerAsActionInfo
import com.harana.sdk.shared.models.flow.catalogs.ActionCategory.Transformation.TextProcessing
import com.harana.sdk.shared.models.flow.documentation.SparkActionDocumentation
import com.harana.sdk.shared.models.flow.utils.Id

import izumi.reflect.Tag

trait ConvertToNGramsInfo extends TransformerAsActionInfo[NGramTransformerInfo] with SparkActionDocumentation {

  val id: Id = "06a73bfe-4e1a-4cde-ae6c-ad5a31f72496"
  val name = "convert-to-ngrams"
  val since = Version(1,0,0)
  val docsGuideLocation = Some("ml-features.html#n-gram")
  val category = TextProcessing

  lazy val portO_1: Tag[NGramTransformerInfo] = typeTag

}

object ConvertToNGramsInfo extends ConvertToNGramsInfo