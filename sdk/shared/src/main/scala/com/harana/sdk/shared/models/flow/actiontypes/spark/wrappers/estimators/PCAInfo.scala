package com.harana.sdk.shared.models.flow.actiontypes.spark.wrappers.estimators

import com.harana.sdk.shared.models.common.Version
import com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.estimators.PCAEstimatorInfo
import com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.models.PCAModelInfo
import com.harana.sdk.shared.models.flow.actiontypes.EstimatorAsActionInfo
import com.harana.sdk.shared.models.flow.catalogs.ActionCategory.ML.DimensionalityReduction
import com.harana.sdk.shared.models.flow.documentation.SparkActionDocumentation
import com.harana.sdk.shared.models.flow.utils.Id

import izumi.reflect.Tag

trait PCAInfo extends EstimatorAsActionInfo[PCAEstimatorInfo, PCAModelInfo] with SparkActionDocumentation {

  val id: Id = "fe1ac5fa-329a-4e3e-9cfc-67ee165053db"
  val name = "pca"
  val since = Version(1,0,0)
  val docsGuideLocation = Some("mllib-dimensionality-reduction.html#principal-component-analysis-pca")
  val category = DimensionalityReduction

  lazy val tTagInfoE: Tag[PCAEstimatorInfo] = typeTag
  lazy val portO_1: Tag[PCAModelInfo] = typeTag

}

object PCAInfo extends PCAInfo