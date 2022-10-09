package com.harana.sdk.shared.models.flow.actiontypes.spark.wrappers.estimators

import com.harana.sdk.shared.models.common.Version
import com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.estimators.RandomForestClassifierInfo
import com.harana.sdk.shared.models.flow.actiontypes.EstimatorAsFactoryInfo
import com.harana.sdk.shared.models.flow.catalogs.ActionCategory.ML.Classification
import com.harana.sdk.shared.models.flow.documentation.SparkActionDocumentation
import com.harana.sdk.shared.models.flow.utils.Id

import izumi.reflect.Tag

trait CreateRandomForestClassifierInfo extends EstimatorAsFactoryInfo[RandomForestClassifierInfo] with SparkActionDocumentation {

  val id: Id = "7cd334e2-bd40-42db-bea1-7592f12302f2"
  val name = "random-forest-classifier"
  val since = Version(1,0,0)
  val docsGuideLocation = Some("ml-classification-regression.html#random-forest-classifier")
  val category = Classification

  lazy val portO_0: Tag[RandomForestClassifierInfo] = typeTag

}

object CreateRandomForestClassifierInfo extends CreateRandomForestClassifierInfo