package com.harana.sdk.shared.models.flow.actiontypes.spark.wrappers.estimators

import com.harana.sdk.shared.models.common.Version
import com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.estimators.GBTClassifierInfo
import com.harana.sdk.shared.models.flow.actiontypes.EstimatorAsFactoryInfo
import com.harana.sdk.shared.models.flow.catalogs.ActionCategory.ML.Classification
import com.harana.sdk.shared.models.flow.documentation.SparkActionDocumentation
import com.harana.sdk.shared.models.flow.utils.Id

import izumi.reflect.Tag

trait CreateGBTClassifierInfo extends EstimatorAsFactoryInfo[GBTClassifierInfo] with SparkActionDocumentation {

  val id: Id = "98275271-9817-4add-85d7-e6eade3e5b81"
  val name = "gbt-classifier"
  val since = Version(1,0,0)
  val docsGuideLocation = Some("ml-classification-regression.html#gradient-boosted-tree-classifier")
  val category = Classification

  lazy val portO_0: Tag[GBTClassifierInfo] = typeTag

}

object CreateGBTClassifierInfo extends CreateGBTClassifierInfo