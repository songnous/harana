package com.harana.sdk.shared.models.flow.actiontypes.spark.wrappers.estimators

import com.harana.sdk.shared.models.common.Version
import com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.estimators.Word2VecEstimatorInfo
import com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.models.Word2VecModelInfo
import com.harana.sdk.shared.models.flow.actiontypes.EstimatorAsActionInfo
import com.harana.sdk.shared.models.flow.catalogs.ActionCategory.Transformation.TextProcessing
import com.harana.sdk.shared.models.flow.documentation.SparkActionDocumentation
import com.harana.sdk.shared.models.flow.utils.Id

trait Word2VecInfo extends EstimatorAsActionInfo[Word2VecEstimatorInfo, Word2VecModelInfo] with SparkActionDocumentation {

  val id: Id = "131c6765-6b60-44c7-9a09-0f79fbb4ad2f"
  val name = "word2vec"
  val since = Version(1,0,0)
  val docsGuideLocation = Some("ml-features.html#word2vec")
  val category = TextProcessing

  lazy val tTagInfoE = typeTag[Word2VecEstimatorInfo]
  lazy val portO_1 = typeTag[Word2VecModelInfo]

}

object Word2VecInfo extends Word2VecInfo