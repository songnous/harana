package com.harana.sdk.shared.models.flow.actiontypes.spark.wrappers.estimators

import com.harana.sdk.shared.models.common.Version
import com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.estimators.LinearRegressionInfo
import com.harana.sdk.shared.models.flow.actiontypes.EstimatorAsFactoryInfo
import com.harana.sdk.shared.models.flow.catalogs.ActionCategory.ML.Regression
import com.harana.sdk.shared.models.flow.documentation.SparkActionDocumentation
import com.harana.sdk.shared.models.flow.utils.Id

import izumi.reflect.Tag

trait CreateLinearRegressionInfo extends EstimatorAsFactoryInfo[LinearRegressionInfo] with SparkActionDocumentation {

  val id: Id = "461a7b68-5fc8-4cd7-a912-0e0cc70eb3aa"
  val name = "linear-regression"
  val since = Version(1,0,0)
  val docsGuideLocation = Some("ml-classification-regression.html#linear-regression")
  val category = Regression

  lazy val portO_0: Tag[LinearRegressionInfo] = typeTag

}

object CreateLinearRegressionInfo extends CreateLinearRegressionInfo