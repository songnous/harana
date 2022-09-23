package com.harana.sdk.shared.models.flow.actions.spark.wrappers.estimators

import com.harana.sdk.shared.models.common.Version
import com.harana.sdk.shared.models.flow.catalogs.ActionCategory.Transformation
import com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.estimators.VectorIndexerEstimatorInfo
import com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.evaluators.BinaryClassificationEvaluatorInfo
import com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.models.VectorIndexerModelInfo
import com.harana.sdk.shared.models.flow.actions.EstimatorAsActionInfo
import com.harana.sdk.shared.models.flow.catalogs.ActionCategory.Transformation.FeatureConversion
import com.harana.sdk.shared.models.flow.documentation.SparkActionDocumentation
import com.harana.sdk.shared.models.flow.utils.Id

trait VectorIndexerInfo extends EstimatorAsActionInfo[VectorIndexerEstimatorInfo, VectorIndexerModelInfo] with SparkActionDocumentation {

  val id: Id = "d62abcbf-1540-4d58-8396-a92b017f2ef0"
  val name = "Vector Indexer"
  val since = Version(1,0,0)
  val docsGuideLocation = Some("ml-features.html#vectorindexer")
  val category = FeatureConversion

  lazy val tTagInfoE = typeTag[VectorIndexerEstimatorInfo]
  lazy val portO_1 = typeTag[VectorIndexerModelInfo]

}

object VectorIndexerInfo extends VectorIndexerInfo {
  def apply() = new VectorIndexerInfo {}
}