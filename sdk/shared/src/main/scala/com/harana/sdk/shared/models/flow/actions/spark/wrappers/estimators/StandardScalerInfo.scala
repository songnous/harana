package com.harana.sdk.shared.models.flow.actions.spark.wrappers.estimators

import com.harana.sdk.shared.models.common.Version
import com.harana.sdk.shared.models.flow.catalogs.ActionCategory.Transformation
import com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.estimators.StandardScalerEstimatorInfo
import com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.models.StandardScalerModelInfo
import com.harana.sdk.shared.models.flow.actions.EstimatorAsActionInfo
import com.harana.sdk.shared.models.flow.catalogs.ActionCategory.Transformation.FeatureScaling
import com.harana.sdk.shared.models.flow.documentation.SparkActionDocumentation
import com.harana.sdk.shared.models.flow.utils.Id

import scala.reflect.runtime.universe.TypeTag

trait StandardScalerInfo extends EstimatorAsActionInfo[StandardScalerEstimatorInfo, StandardScalerModelInfo] with SparkActionDocumentation {

  val id: Id = "85007b46-210c-4e88-b7dc-cf46d3803b06"
  val name = "Standard Scaler"
  val description = "Standardizes features by removing the mean and scaling to unit variance using column summary statistics on the samples in the training set"
  val since = Version(1,0,0)
  val docsGuideLocation = Some("ml-features.html#standardscaler")
  val category = FeatureScaling

  lazy val tTagInfoE: TypeTag[StandardScalerEstimatorInfo] = typeTag
  lazy val portO_1: TypeTag[StandardScalerModelInfo] = typeTag

}

object StandardScalerInfo extends StandardScalerInfo