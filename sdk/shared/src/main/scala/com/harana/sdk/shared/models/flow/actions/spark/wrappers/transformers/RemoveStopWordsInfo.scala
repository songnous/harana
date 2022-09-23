package com.harana.sdk.shared.models.flow.actions.spark.wrappers.transformers

import com.harana.sdk.shared.models.common.Version
import com.harana.sdk.shared.models.flow.catalogs.ActionCategory.Transformation
import com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.transformers.StopWordsRemoverInfo
import com.harana.sdk.shared.models.flow.actions.TransformerAsActionInfo
import com.harana.sdk.shared.models.flow.catalogs.ActionCategory.Transformation.TextProcessing
import com.harana.sdk.shared.models.flow.documentation.SparkActionDocumentation
import com.harana.sdk.shared.models.flow.utils.Id

import scala.reflect.runtime.universe.TypeTag

trait RemoveStopWordsInfo extends TransformerAsActionInfo[StopWordsRemoverInfo] with SparkActionDocumentation {

  val id: Id = "39acf60c-3f57-4346-ada7-6959a76568a5"
  val name = "Remove Stop Words"
  val since = Version(1,0,0)
  val docsGuideLocation = Some("ml-features.html#stopwordsremover")
  val category = TextProcessing

  lazy val tTagT: TypeTag[StopWordsRemoverInfo] = typeTag
  lazy val portO_1: TypeTag[StopWordsRemoverInfo] = typeTag

}

object RemoveStopWordsInfo extends RemoveStopWordsInfo {
  def apply() = new RemoveStopWordsInfo {}
}