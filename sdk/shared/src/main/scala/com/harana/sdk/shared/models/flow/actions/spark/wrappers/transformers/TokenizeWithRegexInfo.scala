package com.harana.sdk.shared.models.flow.actions.spark.wrappers.transformers

import com.harana.sdk.shared.models.common.Version
import com.harana.sdk.shared.models.flow.catalogs.ActionCategory.Transformation
import com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.transformers.RegexTokenizerInfo
import com.harana.sdk.shared.models.flow.actions.TransformerAsActionInfo
import com.harana.sdk.shared.models.flow.catalogs.ActionCategory.Transformation.TextProcessing
import com.harana.sdk.shared.models.flow.documentation.SparkActionDocumentation
import com.harana.sdk.shared.models.flow.utils.Id

import scala.reflect.runtime.universe.TypeTag

trait TokenizeWithRegexInfo extends TransformerAsActionInfo[RegexTokenizerInfo] with SparkActionDocumentation {

  val id: Id = "3fb50e0a-d4fb-474f-b6f3-679788068b1b"
  val name = "Tokenize With Regex"
  val since = Version(1,0,0)
  val docsGuideLocation = Some("ml-features.html#tokenizer")
  val category = TextProcessing

  lazy val portO_1: TypeTag[RegexTokenizerInfo] = typeTag

}

object TokenizeWithRegexInfo extends TokenizeWithRegexInfo {
  def apply() = new TokenizeWithRegexInfo {}
}