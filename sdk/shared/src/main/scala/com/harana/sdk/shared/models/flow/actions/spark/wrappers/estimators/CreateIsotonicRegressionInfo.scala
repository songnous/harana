package com.harana.sdk.shared.models.flow.actions.spark.wrappers.estimators

import com.harana.sdk.shared.models.common.Version
import com.harana.sdk.shared.models.flow.catalogs.ActionCategory.ML
import com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.estimators.{DecisionTreeRegressionInfo, IsotonicRegressionInfo}
import com.harana.sdk.shared.models.flow.actions.EstimatorAsFactoryInfo
import com.harana.sdk.shared.models.flow.catalogs.ActionCategory.ML.Regression
import com.harana.sdk.shared.models.flow.documentation.SparkActionDocumentation
import com.harana.sdk.shared.models.flow.utils.Id

import scala.reflect.runtime.universe.TypeTag

trait CreateIsotonicRegressionInfo extends EstimatorAsFactoryInfo[IsotonicRegressionInfo] with SparkActionDocumentation {

  val id: Id = "0aebeb36-058c-49ef-a1be-7974ef56b564"
  val name = "Isotonic Regression"
  val since = Version(1,0,0)
  val docsGuideLocation = Some("mllib-isotonic-regression.html")
  val category = Regression

  lazy val portO_0: TypeTag[IsotonicRegressionInfo] = typeTag

}

object CreateIsotonicRegressionInfo extends CreateIsotonicRegressionInfo {
  def apply(pos: (Int, Int), color: Option[String] = None) = new CreateIsotonicRegressionInfo {
    override val position = Some(pos)
    override val overrideColor = color
  }
}