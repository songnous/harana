package com.harana.sdk.shared.models.flow.actions.spark.wrappers.estimators

import com.harana.sdk.shared.models.common.Version
import com.harana.sdk.shared.models.flow.catalogs.ActionCategory.ML
import com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.estimators.UnivariateFeatureEstimatorInfo
import com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.evaluators.RegressionEvaluatorInfo
import com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.models.UnivariateFeatureModelInfo
import com.harana.sdk.shared.models.flow.actions.EstimatorAsActionInfo
import com.harana.sdk.shared.models.flow.catalogs.ActionCategory.ML.FeatureSelection
import com.harana.sdk.shared.models.flow.documentation.SparkActionDocumentation
import com.harana.sdk.shared.models.flow.utils.Id

import scala.reflect.runtime.universe.TypeTag

trait UnivariateFeatureSelectorInfo extends EstimatorAsActionInfo[UnivariateFeatureEstimatorInfo, UnivariateFeatureModelInfo] with SparkActionDocumentation {

  val id: Id = "7355518a-4581-4048-b8b2-880cdb212205"
  val name = "Chi-Squared Selector"
  val since = Version(1,0,0)
  val docsGuideLocation = Some("ml-features.html#chisqselector")
  val category = FeatureSelection

  lazy val tTagInfoE: TypeTag[UnivariateFeatureEstimatorInfo] = typeTag
  lazy val portO_1: TypeTag[UnivariateFeatureModelInfo] = typeTag

}

object UnivariateFeatureSelectorInfo extends UnivariateFeatureSelectorInfo