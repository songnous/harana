package com.harana.sdk.shared.models.flow.actions.spark.wrappers.estimators

import com.harana.sdk.shared.models.common.Version
import com.harana.sdk.shared.models.flow.catalogs.ActionCategory.ML
import com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.estimators.{DecisionTreeRegressionInfo, RandomForestRegressionInfo}
import com.harana.sdk.shared.models.flow.actions.EstimatorAsFactoryInfo
import com.harana.sdk.shared.models.flow.catalogs.ActionCategory.ML.Regression
import com.harana.sdk.shared.models.flow.documentation.SparkActionDocumentation
import com.harana.sdk.shared.models.flow.utils.Id

import scala.reflect.runtime.universe.TypeTag

trait CreateRandomForestRegressionInfo extends EstimatorAsFactoryInfo[RandomForestRegressionInfo] with SparkActionDocumentation {

  val id: Id = "2ec65504-bbe2-4ba2-a9b4-192e2f45ff16"
  val name = "Random Forest Regression"
  val since = Version(1,0,0)
  val docsGuideLocation = Some("ml-classification-regression.html#random-forest-regression")
  val category = Regression

  lazy val portO_0: TypeTag[RandomForestRegressionInfo] = typeTag

}

object CreateRandomForestRegressionInfo extends CreateRandomForestRegressionInfo {
  def apply(pos: (Int, Int), color: Option[String] = None) = new CreateRandomForestRegressionInfo {
    override val position = Some(pos)
    override val overrideColor = color
  }
}