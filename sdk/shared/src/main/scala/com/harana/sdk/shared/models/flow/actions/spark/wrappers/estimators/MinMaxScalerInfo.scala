package com.harana.sdk.shared.models.flow.actions.spark.wrappers.estimators

import com.harana.sdk.shared.models.common.Version
import com.harana.sdk.shared.models.flow.catalogs.ActionCategory.Transformation
import com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.estimators.MinMaxScalerEstimatorInfo
import com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.models.MinMaxScalerModelInfo
import com.harana.sdk.shared.models.flow.actions.EstimatorAsActionInfo
import com.harana.sdk.shared.models.flow.catalogs.ActionCategory.Transformation.FeatureScaling
import com.harana.sdk.shared.models.flow.documentation.SparkActionDocumentation
import com.harana.sdk.shared.models.flow.utils.Id

import scala.reflect.runtime.universe.TypeTag

trait MinMaxScalerInfo extends EstimatorAsActionInfo[MinMaxScalerEstimatorInfo, MinMaxScalerModelInfo] with SparkActionDocumentation {

  val id: Id = "a63b6de3-793b-4cbd-ae81-76de216d90d5"
  val name = "Min-Max Scaler"
  val since = Version(1,0,0)
  val docsGuideLocation = Some("ml-features.html#minmaxscaler")
  val category = FeatureScaling

  lazy val tTagInfoE: TypeTag[MinMaxScalerEstimatorInfo] = typeTag
  lazy val portO_1: TypeTag[MinMaxScalerModelInfo] = typeTag

}

object MinMaxScalerInfo extends MinMaxScalerInfo {
  def apply() = new MinMaxScalerInfo {}
}