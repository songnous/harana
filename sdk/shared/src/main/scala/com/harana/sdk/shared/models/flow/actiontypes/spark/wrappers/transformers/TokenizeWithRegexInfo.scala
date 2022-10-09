package com.harana.sdk.shared.models.flow.actiontypes.spark.wrappers.transformers

import com.harana.sdk.shared.models.common.Version
import com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.transformers.RegexTokenizerInfo
import com.harana.sdk.shared.models.flow.actiontypes.TransformerAsActionInfo
import com.harana.sdk.shared.models.flow.catalogs.ActionCategory.Transformation.TextProcessing
import com.harana.sdk.shared.models.flow.documentation.SparkActionDocumentation
import com.harana.sdk.shared.models.flow.utils.Id

import izumi.reflect.Tag

trait TokenizeWithRegexInfo extends TransformerAsActionInfo[RegexTokenizerInfo] with SparkActionDocumentation {

  val id: Id = "3fb50e0a-d4fb-474f-b6f3-679788068b1b"
  val name = "tokenize-with-regex"
  val since = Version(1,0,0)
  val docsGuideLocation = Some("ml-features.html#tokenizer")
  val category = TextProcessing

  lazy val portO_1: Tag[RegexTokenizerInfo] = typeTag

}

object TokenizeWithRegexInfo extends TokenizeWithRegexInfo