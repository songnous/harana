package com.harana.sdk.shared.models.flow.actiontypes.spark.wrappers.estimators

import com.harana.sdk.shared.models.common.Version
import com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.estimators.UnivariateFeatureSelectorEstimatorInfo
import com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.models.UnivariateFeatureSelectorModelInfo
import com.harana.sdk.shared.models.flow.actiontypes.EstimatorAsActionInfo
import com.harana.sdk.shared.models.flow.catalogs.ActionCategory.ML.FeatureSelection
import com.harana.sdk.shared.models.flow.documentation.SparkActionDocumentation
import com.harana.sdk.shared.models.flow.utils.Id
import izumi.reflect.Tag

trait UnivariateFeatureSelectorInfo extends EstimatorAsActionInfo[UnivariateFeatureSelectorEstimatorInfo, UnivariateFeatureSelectorModelInfo] with SparkActionDocumentation {

  val id: Id = "7355518a-4581-4048-b8b2-880cdb212205"
  val name = "chi-squared-selector"
  val since = Version(1,0,0)
  val docsGuideLocation = Some("ml-features.html#chisqselector")
  val category = FeatureSelection

  lazy val tTagInfoE: Tag[UnivariateFeatureSelectorEstimatorInfo] = typeTag
  lazy val portO_1: Tag[UnivariateFeatureSelectorModelInfo] = typeTag

}

object UnivariateFeatureSelectorInfo extends UnivariateFeatureSelectorInfo