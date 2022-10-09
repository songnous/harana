package com.harana.sdk.shared.models.flow.actiontypes.spark.wrappers.transformers

import com.harana.sdk.shared.models.common.Version
import com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.transformers.StopWordsRemoverInfo
import com.harana.sdk.shared.models.flow.actiontypes.TransformerAsActionInfo
import com.harana.sdk.shared.models.flow.catalogs.ActionCategory.Transformation.TextProcessing
import com.harana.sdk.shared.models.flow.documentation.SparkActionDocumentation
import com.harana.sdk.shared.models.flow.utils.Id

import izumi.reflect.Tag

trait RemoveStopWordsInfo extends TransformerAsActionInfo[StopWordsRemoverInfo] with SparkActionDocumentation {

  val id: Id = "39acf60c-3f57-4346-ada7-6959a76568a5"
  val name = "remove-stop-words"
  val since = Version(1,0,0)
  val docsGuideLocation = Some("ml-features.html#stopwordsremover")
  val category = TextProcessing

  lazy val tTagT: Tag[StopWordsRemoverInfo] = typeTag
  lazy val portO_1: Tag[StopWordsRemoverInfo] = typeTag

}

object RemoveStopWordsInfo extends RemoveStopWordsInfo