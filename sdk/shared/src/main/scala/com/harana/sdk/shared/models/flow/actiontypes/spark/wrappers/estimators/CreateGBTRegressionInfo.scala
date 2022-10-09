package com.harana.sdk.shared.models.flow.actiontypes.spark.wrappers.estimators

import com.harana.sdk.shared.models.common.Version
import com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.estimators.GBTRegressionInfo
import com.harana.sdk.shared.models.flow.actiontypes.EstimatorAsFactoryInfo
import com.harana.sdk.shared.models.flow.catalogs.ActionCategory.ML.Regression
import com.harana.sdk.shared.models.flow.documentation.SparkActionDocumentation
import com.harana.sdk.shared.models.flow.utils.Id

import izumi.reflect.Tag


trait CreateGBTRegressionInfo extends EstimatorAsFactoryInfo[GBTRegressionInfo] with SparkActionDocumentation {

  val id: Id = "e18c13f8-2108-46f0-979f-bba5a11ea312"
  val name = "gbt-regression"
  val since = Version(1,0,0)
  val docsGuideLocation = Some("ml-classification-regression.html#gradient-boosted-tree-regression")
  val category = Regression

  lazy val portO_0: Tag[GBTRegressionInfo] = typeTag

}

object CreateGBTRegressionInfo extends CreateGBTRegressionInfo