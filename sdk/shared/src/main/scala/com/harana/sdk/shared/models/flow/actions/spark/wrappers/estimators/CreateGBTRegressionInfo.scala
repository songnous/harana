package com.harana.sdk.shared.models.flow.actions.spark.wrappers.estimators

import com.harana.sdk.shared.models.common.Version
import com.harana.sdk.shared.models.flow.catalogs.ActionCategory.ML
import com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.estimators.{DecisionTreeRegressionInfo, GBTRegressionInfo}
import com.harana.sdk.shared.models.flow.actions.EstimatorAsFactoryInfo
import com.harana.sdk.shared.models.flow.catalogs.ActionCategory.ML.Regression
import com.harana.sdk.shared.models.flow.documentation.SparkActionDocumentation
import com.harana.sdk.shared.models.flow.utils.Id

import scala.reflect.runtime.universe.TypeTag


trait CreateGBTRegressionInfo extends EstimatorAsFactoryInfo[GBTRegressionInfo] with SparkActionDocumentation {

  val id: Id = "e18c13f8-2108-46f0-979f-bba5a11ea312"
  val name = "GBT Regression"
  val description = """Gradient-Boosted Trees (GBTs) is a learning algorithm for regression. It supports both continuous and categorical features.""".stripMargin
  val since = Version(1,0,0)
  val docsGuideLocation = Some("ml-classification-regression.html#gradient-boosted-tree-regression")
  val category = Regression

  lazy val portO_0: TypeTag[GBTRegressionInfo] = typeTag

}

object CreateGBTRegressionInfo extends CreateGBTRegressionInfo