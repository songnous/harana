package com.harana.sdk.shared.models.flow.actiontypes.spark.wrappers.estimators

import com.harana.sdk.shared.models.common.Version
import com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.estimators.DecisionTreeClassifierInfo
import com.harana.sdk.shared.models.flow.actiontypes.EstimatorAsFactoryInfo
import com.harana.sdk.shared.models.flow.catalogs.ActionCategory.ML.Classification
import com.harana.sdk.shared.models.flow.documentation.SparkActionDocumentation
import com.harana.sdk.shared.models.flow.utils.Id

import izumi.reflect.Tag

trait CreateDecisionTreeClassifierInfo extends EstimatorAsFactoryInfo[DecisionTreeClassifierInfo] with SparkActionDocumentation {

  val id: Id = "81039036-bb26-445b-81b5-63fbc9295c00"
  val name = "decision-tree-classifier"
  val since = Version(1,0,0)
  val docsGuideLocation = Some("ml-classification-regression.html#decision-tree-classifier")
  val category = Classification

  lazy val portO_0: Tag[DecisionTreeClassifierInfo] = typeTag

}

object CreateDecisionTreeClassifierInfo extends CreateDecisionTreeClassifierInfo