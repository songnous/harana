package com.harana.sdk.shared.models.flow.actiontypes.spark.wrappers.transformers

import com.harana.sdk.shared.models.common.Version
import com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.transformers.StringTokenizerInfo
import com.harana.sdk.shared.models.flow.actiontypes.TransformerAsActionInfo
import com.harana.sdk.shared.models.flow.catalogs.ActionCategory.Transformation.TextProcessing
import com.harana.sdk.shared.models.flow.documentation.SparkActionDocumentation
import com.harana.sdk.shared.models.flow.utils.Id

import izumi.reflect.Tag

trait TokenizeInfo extends TransformerAsActionInfo[StringTokenizerInfo] with SparkActionDocumentation {

  val id: Id = "38751243-5e0e-435a-b366-8d225c9fd5ca"
  val name = "tokenize"
  val since = Version(1,0,0)
  val docsGuideLocation = Some("ml-features.html#tokenizer")
  val category = TextProcessing

  lazy val portO_1: Tag[StringTokenizerInfo] = typeTag

}

object TokenizeInfo extends TokenizeInfo