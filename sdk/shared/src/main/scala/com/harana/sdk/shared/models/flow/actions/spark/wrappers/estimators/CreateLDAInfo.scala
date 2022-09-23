package com.harana.sdk.shared.models.flow.actions.spark.wrappers.estimators

import com.harana.sdk.shared.models.common.Version
import com.harana.sdk.shared.models.flow.catalogs.ActionCategory.ML
import com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.estimators.{DecisionTreeRegressionInfo, LDAInfo}
import com.harana.sdk.shared.models.flow.actions.EstimatorAsFactoryInfo
import com.harana.sdk.shared.models.flow.catalogs.ActionCategory.ML.Clustering
import com.harana.sdk.shared.models.flow.documentation.SparkActionDocumentation
import com.harana.sdk.shared.models.flow.utils.Id

import scala.reflect.runtime.universe.TypeTag

trait CreateLDAInfo extends EstimatorAsFactoryInfo[LDAInfo] {

  val id: Id = "a385f8fe-c64e-4d71-870a-9d5048747a3c"
  val name = "LDA"
  val since = Version(1,0,0)
  val docsGuideLocation = Some("ml-clustering.html#lda")
  val category = Clustering

  lazy val portO_0: TypeTag[LDAInfo] = typeTag

}

object CreateLDAInfo extends CreateLDAInfo {
  def apply() = new CreateLDAInfo {}
}