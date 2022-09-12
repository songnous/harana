package com.harana.sdk.shared.models.flow.actions.spark.wrappers.estimators

import com.harana.sdk.shared.models.common.Version
import com.harana.sdk.shared.models.flow.catalogs.ActionCategory.ML
import com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.estimators.{AFTSurvivalRegressionInfo, ALSInfo}
import com.harana.sdk.shared.models.flow.actions.EstimatorAsFactoryInfo
import com.harana.sdk.shared.models.flow.catalogs.ActionCategory.ML.Recommendation
import com.harana.sdk.shared.models.flow.documentation.SparkActionDocumentation
import com.harana.sdk.shared.models.flow.utils.Id

import scala.reflect.runtime.universe.TypeTag

trait CreateALSInfo extends EstimatorAsFactoryInfo[ALSInfo] with SparkActionDocumentation {

  val id: Id = "5a9e4883-b653-418e-bc51-a42fde476a63"
  val name = "ALS"
  val description = "Creates an ALS recommendation model"
  val since = Version(1,0,0)
  val docsGuideLocation = Some("mllib-collaborative-filtering.html#collaborative-filtering")
  val category = Recommendation

  lazy val portO_0: TypeTag[ALSInfo] = typeTag

}

object CreateALSInfo extends CreateALSInfo