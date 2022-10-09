package com.harana.sdk.shared.models.flow.actiontypes.spark.wrappers.estimators

import com.harana.sdk.shared.models.common.Version
import com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.estimators.CountVectorizerEstimatorInfo
import com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.models.CountVectorizerModelInfo
import com.harana.sdk.shared.models.flow.actiontypes.EstimatorAsActionInfo
import com.harana.sdk.shared.models.flow.catalogs.ActionCategory.Transformation.TextProcessing
import com.harana.sdk.shared.models.flow.documentation.SparkActionDocumentation
import com.harana.sdk.shared.models.flow.utils.Id

import izumi.reflect.Tag

trait CountVectorizerInfo extends EstimatorAsActionInfo[CountVectorizerEstimatorInfo, CountVectorizerModelInfo] with SparkActionDocumentation {

  val id: Id = "e640d7df-d464-4ac0-99c4-235c29a0aa31"
  val name = "count-vectorizer"
  val since = Version(1,0,0)
  val docsGuideLocation = Some("ml-features.html#countvectorizer")
  val category = TextProcessing

  lazy val tTagInfoE: Tag[CountVectorizerEstimatorInfo] = typeTag
  lazy val portO_1: Tag[CountVectorizerModelInfo] = typeTag

}

object CountVectorizerInfo extends CountVectorizerInfo