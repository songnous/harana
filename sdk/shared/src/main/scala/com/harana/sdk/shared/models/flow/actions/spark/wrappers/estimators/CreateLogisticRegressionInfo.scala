package com.harana.sdk.shared.models.flow.actions.spark.wrappers.estimators

import com.harana.sdk.shared.models.common.Version
import com.harana.sdk.shared.models.flow.catalogs.ActionCategory.ML
import com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.estimators.LogisticRegressionInfo
import com.harana.sdk.shared.models.flow.actions.EstimatorAsFactoryInfo
import com.harana.sdk.shared.models.flow.catalogs.ActionCategory.ML.Classification
import com.harana.sdk.shared.models.flow.documentation.SparkActionDocumentation
import com.harana.sdk.shared.models.flow.utils.Id

import scala.reflect.runtime.universe.TypeTag

trait CreateLogisticRegressionInfo extends EstimatorAsFactoryInfo[LogisticRegressionInfo] with SparkActionDocumentation {

  val id: Id = "7f9e459e-3e11-4c5f-9137-94447d53ff60"
  val name = "Logistic Regression"
  val description = "Creates a logistic regression model"
  val since = Version(1,0,0)
  val docsGuideLocation = Some("ml-classification-regression.html#logistic-regression")
  val category = Classification

  lazy val portO_0: TypeTag[LogisticRegressionInfo] = typeTag

}

object CreateLogisticRegressionInfo extends CreateLogisticRegressionInfo