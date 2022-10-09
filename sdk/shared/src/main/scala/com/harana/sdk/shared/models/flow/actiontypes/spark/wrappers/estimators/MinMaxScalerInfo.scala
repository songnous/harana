package com.harana.sdk.shared.models.flow.actiontypes.spark.wrappers.estimators

import com.harana.sdk.shared.models.common.Version
import com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.estimators.MinMaxScalerEstimatorInfo
import com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.models.MinMaxScalerModelInfo
import com.harana.sdk.shared.models.flow.actiontypes.EstimatorAsActionInfo
import com.harana.sdk.shared.models.flow.catalogs.ActionCategory.Transformation.FeatureScaling
import com.harana.sdk.shared.models.flow.documentation.SparkActionDocumentation
import com.harana.sdk.shared.models.flow.utils.Id

import izumi.reflect.Tag

trait MinMaxScalerInfo extends EstimatorAsActionInfo[MinMaxScalerEstimatorInfo, MinMaxScalerModelInfo] with SparkActionDocumentation {

  val id: Id = "a63b6de3-793b-4cbd-ae81-76de216d90d5"
  val name = "min-max-scaler"
  val since = Version(1,0,0)
  val docsGuideLocation = Some("ml-features.html#minmaxscaler")
  val category = FeatureScaling

  lazy val tTagInfoE: Tag[MinMaxScalerEstimatorInfo] = typeTag
  lazy val portO_1: Tag[MinMaxScalerModelInfo] = typeTag

}

object MinMaxScalerInfo extends MinMaxScalerInfo