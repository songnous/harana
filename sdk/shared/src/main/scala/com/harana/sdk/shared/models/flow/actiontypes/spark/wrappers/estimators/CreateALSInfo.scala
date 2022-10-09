package com.harana.sdk.shared.models.flow.actiontypes.spark.wrappers.estimators

import com.harana.sdk.shared.models.common.Version
import com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.estimators.ALSInfo
import com.harana.sdk.shared.models.flow.actiontypes.EstimatorAsFactoryInfo
import com.harana.sdk.shared.models.flow.catalogs.ActionCategory.ML.Recommendation
import com.harana.sdk.shared.models.flow.documentation.SparkActionDocumentation
import com.harana.sdk.shared.models.flow.utils.Id

import izumi.reflect.Tag

trait CreateALSInfo extends EstimatorAsFactoryInfo[ALSInfo] with SparkActionDocumentation {

  val id: Id = "5a9e4883-b653-418e-bc51-a42fde476a63"
  val name = "als"
  val since = Version(1,0,0)
  val docsGuideLocation = Some("mllib-collaborative-filtering.html#collaborative-filtering")
  val category = Recommendation

  lazy val portO_0: Tag[ALSInfo] = typeTag

}

object CreateALSInfo extends CreateALSInfo