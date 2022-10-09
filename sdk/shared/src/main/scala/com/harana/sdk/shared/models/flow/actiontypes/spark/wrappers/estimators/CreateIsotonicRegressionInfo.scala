package com.harana.sdk.shared.models.flow.actiontypes.spark.wrappers.estimators

import com.harana.sdk.shared.models.common.Version
import com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.estimators.IsotonicRegressionInfo
import com.harana.sdk.shared.models.flow.actiontypes.EstimatorAsFactoryInfo
import com.harana.sdk.shared.models.flow.catalogs.ActionCategory.ML.Regression
import com.harana.sdk.shared.models.flow.documentation.SparkActionDocumentation
import com.harana.sdk.shared.models.flow.utils.Id

import izumi.reflect.Tag

trait CreateIsotonicRegressionInfo extends EstimatorAsFactoryInfo[IsotonicRegressionInfo] with SparkActionDocumentation {

  val id: Id = "0aebeb36-058c-49ef-a1be-7974ef56b564"
  val name = "isotonic-regression"
  val since = Version(1,0,0)
  val docsGuideLocation = Some("mllib-isotonic-regression.html")
  val category = Regression

  lazy val portO_0: Tag[IsotonicRegressionInfo] = typeTag

}

object CreateIsotonicRegressionInfo extends CreateIsotonicRegressionInfo