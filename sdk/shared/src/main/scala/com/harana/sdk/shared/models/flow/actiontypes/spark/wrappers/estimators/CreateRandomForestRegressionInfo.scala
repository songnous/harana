package com.harana.sdk.shared.models.flow.actiontypes.spark.wrappers.estimators

import com.harana.sdk.shared.models.common.Version
import com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.estimators.RandomForestRegressionInfo
import com.harana.sdk.shared.models.flow.actiontypes.EstimatorAsFactoryInfo
import com.harana.sdk.shared.models.flow.catalogs.ActionCategory.ML.Regression
import com.harana.sdk.shared.models.flow.documentation.SparkActionDocumentation
import com.harana.sdk.shared.models.flow.utils.Id

import izumi.reflect.Tag

trait CreateRandomForestRegressionInfo extends EstimatorAsFactoryInfo[RandomForestRegressionInfo] with SparkActionDocumentation {

  val id: Id = "2ec65504-bbe2-4ba2-a9b4-192e2f45ff16"
  val name = "random-forest-regression"
  val since = Version(1,0,0)
  val docsGuideLocation = Some("ml-classification-regression.html#random-forest-regression")
  val category = Regression

  lazy val portO_0: Tag[RandomForestRegressionInfo] = typeTag

}

object CreateRandomForestRegressionInfo extends CreateRandomForestRegressionInfo