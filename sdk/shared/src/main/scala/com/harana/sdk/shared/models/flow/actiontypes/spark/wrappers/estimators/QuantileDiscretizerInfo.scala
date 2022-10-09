package com.harana.sdk.shared.models.flow.actiontypes.spark.wrappers.estimators

import com.harana.sdk.shared.models.common.Version
import com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.estimators.QuantileDiscretizerEstimatorInfo
import com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.models.QuantileDiscretizerModelInfo
import com.harana.sdk.shared.models.flow.actiontypes.EstimatorAsActionInfo
import com.harana.sdk.shared.models.flow.catalogs.ActionCategory.Transformation.FeatureConversion
import com.harana.sdk.shared.models.flow.documentation.SparkActionDocumentation
import com.harana.sdk.shared.models.flow.utils.Id

import izumi.reflect.Tag

trait QuantileDiscretizerInfo extends EstimatorAsActionInfo[QuantileDiscretizerEstimatorInfo, QuantileDiscretizerModelInfo] with SparkActionDocumentation {

  val id: Id = "986e0b10-09de-44e9-a5b1-1dcc5fb53bd1"
  val name = "quantile-discretizer"
  val since = Version(1,0,0)
  val docsGuideLocation = Some("ml-features.html#quantilediscretizer")
  val category = FeatureConversion

  lazy val tTagInfoE: Tag[QuantileDiscretizerEstimatorInfo] = typeTag
  lazy val portO_1: Tag[QuantileDiscretizerModelInfo] = typeTag

}

object QuantileDiscretizerInfo extends QuantileDiscretizerInfo