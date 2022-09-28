package com.harana.sdk.shared.models.flow.actions.spark.wrappers.estimators

import com.harana.sdk.shared.models.common.Version
import com.harana.sdk.shared.models.flow.catalogs.ActionCategory.Transformation
import com.harana.sdk.shared.models.flow.actionobjects.TransformerInfo
import com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.estimators.CountVectorizerEstimatorInfo
import com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.models.CountVectorizerModelInfo
import com.harana.sdk.shared.models.flow.actions.{EstimatorAsActionInfo, UIActionInfo}
import com.harana.sdk.shared.models.flow.actions.read.ReadTransformerInfo
import com.harana.sdk.shared.models.flow.catalogs.ActionCategory.Transformation.TextProcessing
import com.harana.sdk.shared.models.flow.documentation.SparkActionDocumentation
import com.harana.sdk.shared.models.flow.utils.Id

import scala.reflect.runtime.universe.TypeTag

trait CountVectorizerInfo extends EstimatorAsActionInfo[CountVectorizerEstimatorInfo, CountVectorizerModelInfo] with SparkActionDocumentation {

  val id: Id = "e640d7df-d464-4ac0-99c4-235c29a0aa31"
  val name = "Count Vectorizer"
  val since = Version(1,0,0)
  val docsGuideLocation = Some("ml-features.html#countvectorizer")
  val category = TextProcessing

  lazy val tTagInfoE: TypeTag[CountVectorizerEstimatorInfo] = typeTag
  lazy val portO_1: TypeTag[CountVectorizerModelInfo] = typeTag

}

object CountVectorizerInfo extends CountVectorizerInfo with UIActionInfo[CountVectorizerInfo] {
  def apply(pos: (Int, Int), color: Option[String] = None) = new CountVectorizerInfo {
    override val position = Some(pos)
    override val overrideColor = color
  }
}