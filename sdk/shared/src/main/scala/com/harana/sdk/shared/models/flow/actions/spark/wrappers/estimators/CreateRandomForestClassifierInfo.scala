package com.harana.sdk.shared.models.flow.actions.spark.wrappers.estimators

import com.harana.sdk.shared.models.common.Version
import com.harana.sdk.shared.models.flow.catalogs.ActionCategory.ML
import com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.estimators.RandomForestClassifierInfo
import com.harana.sdk.shared.models.flow.actions.EstimatorAsFactoryInfo
import com.harana.sdk.shared.models.flow.catalogs.ActionCategory.ML.Classification
import com.harana.sdk.shared.models.flow.documentation.SparkActionDocumentation
import com.harana.sdk.shared.models.flow.utils.Id

import scala.reflect.runtime.universe.TypeTag

trait CreateRandomForestClassifierInfo extends EstimatorAsFactoryInfo[RandomForestClassifierInfo] with SparkActionDocumentation {

  val id: Id = "7cd334e2-bd40-42db-bea1-7592f12302f2"
  val name = "Random Forest Classifier"
  val since = Version(1,0,0)
  val docsGuideLocation = Some("ml-classification-regression.html#random-forest-classifier")
  val category = Classification

  lazy val portO_0: TypeTag[RandomForestClassifierInfo] = typeTag

}

object CreateRandomForestClassifierInfo extends CreateRandomForestClassifierInfo {
  def apply(pos: (Int, Int), color: Option[String] = None) = new CreateRandomForestClassifierInfo {
    override val position = Some(pos)
    override val overrideColor = color
  }
}