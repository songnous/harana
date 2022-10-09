package com.harana.sdk.shared.models.flow.actiontypes.spark.wrappers.estimators

import com.harana.sdk.shared.models.common.Version
import com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.estimators.LDAInfo
import com.harana.sdk.shared.models.flow.actiontypes.EstimatorAsFactoryInfo
import com.harana.sdk.shared.models.flow.catalogs.ActionCategory.ML.Clustering
import com.harana.sdk.shared.models.flow.utils.Id

import izumi.reflect.Tag

trait CreateLDAInfo extends EstimatorAsFactoryInfo[LDAInfo] {

  val id: Id = "a385f8fe-c64e-4d71-870a-9d5048747a3c"
  val name = "lda"
  val since = Version(1,0,0)
  val docsGuideLocation = Some("ml-clustering.html#lda")
  val category = Clustering

  lazy val portO_0: Tag[LDAInfo] = typeTag

}

object CreateLDAInfo extends CreateLDAInfo