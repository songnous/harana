package com.harana.sdk.shared.models.flow.actiontypes.spark.wrappers.estimators

import com.harana.sdk.shared.models.common.Version
import com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.estimators.AFTSurvivalRegressionInfo
import com.harana.sdk.shared.models.flow.actiontypes.EstimatorAsFactoryInfo
import com.harana.sdk.shared.models.flow.catalogs.ActionCategory.ML.Regression
import com.harana.sdk.shared.models.flow.documentation.SparkActionDocumentation
import com.harana.sdk.shared.models.flow.utils.Id

import izumi.reflect.Tag

trait CreateAFTSurvivalRegressionInfo extends EstimatorAsFactoryInfo[AFTSurvivalRegressionInfo] with SparkActionDocumentation {

  val id: Id = "e315aa7f-16f2-4fa5-8376-69a96171a57a"
  val name = "aft-survival-regression"
  val since = Version(1,0,0)
  val docsGuideLocation = Some("ml-classification-regression.html#survival-regression")
  val category = Regression

  lazy val portO_0: Tag[AFTSurvivalRegressionInfo] = typeTag

}

object CreateAFTSurvivalRegressionInfo extends CreateAFTSurvivalRegressionInfo