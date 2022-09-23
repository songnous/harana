package com.harana.sdk.shared.models.flow.actions.spark.wrappers.estimators

import com.harana.sdk.shared.models.common.Version
import com.harana.sdk.shared.models.flow.catalogs.ActionCategory.ML
import com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.estimators.DecisionTreeClassifierInfo
import com.harana.sdk.shared.models.flow.actions.EstimatorAsFactoryInfo
import com.harana.sdk.shared.models.flow.catalogs.ActionCategory.ML.Classification
import com.harana.sdk.shared.models.flow.documentation.SparkActionDocumentation
import com.harana.sdk.shared.models.flow.utils.Id

import scala.reflect.runtime.universe.TypeTag

trait CreateDecisionTreeClassifierInfo extends EstimatorAsFactoryInfo[DecisionTreeClassifierInfo] with SparkActionDocumentation {

  val id: Id = "81039036-bb26-445b-81b5-63fbc9295c00"
  val name = "Decision Tree Classifier"
  val description = """Creates a decision tree classifier. It supports both binary and multiclass labels, as well as both continuous and categorical features.""".stripMargin
  val since = Version(1,0,0)
  val docsGuideLocation = Some("ml-classification-regression.html#decision-tree-classifier")
  val category = Classification

  lazy val portO_0: TypeTag[DecisionTreeClassifierInfo] = typeTag

}

object CreateDecisionTreeClassifierInfo extends CreateDecisionTreeClassifierInfo {
  def apply() = new CreateDecisionTreeClassifierInfo {}
}