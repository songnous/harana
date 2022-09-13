package com.harana.sdk.shared.models.flow.actions.spark.wrappers.estimators

import com.harana.sdk.shared.models.common.Version
import com.harana.sdk.shared.models.flow.catalogs.ActionCategory.ML
import com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.estimators.{DecisionTreeRegressionInfo, KMeansInfo}
import com.harana.sdk.shared.models.flow.actions.EstimatorAsFactoryInfo
import com.harana.sdk.shared.models.flow.catalogs.ActionCategory.ML.Clustering
import com.harana.sdk.shared.models.flow.documentation.SparkActionDocumentation
import com.harana.sdk.shared.models.flow.utils.Id

import scala.reflect.runtime.universe.TypeTag

trait CreateKMeansInfo extends EstimatorAsFactoryInfo[KMeansInfo] with SparkActionDocumentation {

  val id: Id = "2ecdd789-695d-4efa-98ad-63c80ae70f71"
  val name = "K-Means"
  val description = "Creates a k-means model. Note: Trained k-means model does not have any parameters."
  val since = Version(1,0,0)
  val docsGuideLocation = Some("ml-clustering.html#k-means")
  val category = Clustering

  lazy val portO_0: TypeTag[KMeansInfo] = typeTag

}

object CreateKMeansInfo extends CreateKMeansInfo