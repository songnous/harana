package com.harana.sdk.shared.models.flow.actiontypes.spark.wrappers.estimators

import com.harana.sdk.shared.models.common.Version
import com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.estimators.DecisionTreeRegressionInfo
import com.harana.sdk.shared.models.flow.actiontypes.EstimatorAsFactoryInfo
import com.harana.sdk.shared.models.flow.catalogs.ActionCategory.ML.Regression
import com.harana.sdk.shared.models.flow.documentation.SparkActionDocumentation
import com.harana.sdk.shared.models.flow.utils.Id

import izumi.reflect.Tag

trait CreateDecisionTreeRegressionInfo extends EstimatorAsFactoryInfo[DecisionTreeRegressionInfo] with SparkActionDocumentation {

  val id: Id = "a88db4fb-695e-4f44-8435-4999ccde36de"
  val name = "decision-tree-regression"
  val since = Version(1,0,0)
  val docsGuideLocation = Some("ml-classification-regression.html#decision-tree-regression")
  val category = Regression

  lazy val portO_0: Tag[DecisionTreeRegressionInfo] = typeTag

}

object CreateDecisionTreeRegressionInfo extends CreateDecisionTreeRegressionInfo