package com.harana.sdk.shared.models.flow.actions.spark.wrappers.estimators

import com.harana.sdk.shared.models.common.Version
import com.harana.sdk.shared.models.flow.catalogs.ActionCategory.ML
import com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.estimators.{AFTSurvivalRegressionInfo, DecisionTreeRegressionInfo}
import com.harana.sdk.shared.models.flow.actions.EstimatorAsFactoryInfo
import com.harana.sdk.shared.models.flow.catalogs.ActionCategory.ML.Regression
import com.harana.sdk.shared.models.flow.documentation.SparkActionDocumentation
import com.harana.sdk.shared.models.flow.utils.Id

import scala.reflect.runtime.universe.TypeTag

trait CreateDecisionTreeRegressionInfo extends EstimatorAsFactoryInfo[DecisionTreeRegressionInfo] with SparkActionDocumentation {

  val id: Id = "a88db4fb-695e-4f44-8435-4999ccde36de"
  val name = "Decision Tree Regression"
  val description = """Creates a decision tree regression model. It supports both continuous and categorical features.""".stripMargin
  val since = Version(1,0,0)
  val docsGuideLocation = Some("ml-classification-regression.html#decision-tree-regression")
  val category = Regression

  lazy val portO_0: TypeTag[DecisionTreeRegressionInfo] = typeTag

}

object CreateDecisionTreeRegressionInfo extends CreateDecisionTreeRegressionInfo {
  def apply() = new CreateDecisionTreeRegressionInfo {}
}