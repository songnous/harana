package com.harana.sdk.shared.models.flow.actions.spark.wrappers.estimators

import com.harana.sdk.shared.models.common.Version
import com.harana.sdk.shared.models.flow.catalogs.ActionCategory.ML
import com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.estimators.{DecisionTreeRegressionInfo, GBTClassifierInfo}
import com.harana.sdk.shared.models.flow.actions.EstimatorAsFactoryInfo
import com.harana.sdk.shared.models.flow.catalogs.ActionCategory.ML.Classification
import com.harana.sdk.shared.models.flow.documentation.SparkActionDocumentation
import com.harana.sdk.shared.models.flow.utils.Id

import scala.reflect.runtime.universe.TypeTag

trait CreateGBTClassifierInfo extends EstimatorAsFactoryInfo[GBTClassifierInfo] with SparkActionDocumentation {

  val id: Id = "98275271-9817-4add-85d7-e6eade3e5b81"
  val name = "GBT Classifier"
  val since = Version(1,0,0)
  val docsGuideLocation = Some("ml-classification-regression.html#gradient-boosted-tree-classifier")
  val category = Classification

  lazy val portO_0: TypeTag[GBTClassifierInfo] = typeTag

}

object CreateGBTClassifierInfo extends CreateGBTClassifierInfo {
  def apply(pos: (Int, Int), color: Option[String] = None) = new CreateGBTClassifierInfo {
    override val position = Some(pos)
    override val overrideColor = color
  }
}