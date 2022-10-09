package com.harana.sdk.shared.models.flow.actiontypes.spark.wrappers.estimators

import com.harana.sdk.shared.models.common.Version
import com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.estimators.IDFEstimatorInfo
import com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.models.IDFModelInfo
import com.harana.sdk.shared.models.flow.actiontypes.EstimatorAsActionInfo
import com.harana.sdk.shared.models.flow.catalogs.ActionCategory.Transformation.TextProcessing
import com.harana.sdk.shared.models.flow.documentation.SparkActionDocumentation
import com.harana.sdk.shared.models.flow.utils.Id

import izumi.reflect.Tag

trait IDFInfo extends EstimatorAsActionInfo[IDFEstimatorInfo, IDFModelInfo] with SparkActionDocumentation {

  val id: Id = "36d31a98-9238-4159-8298-64eb8e3ca55a"
  val name = "idf"
  val since = Version(1,0,0)
  val docsGuideLocation = Some("ml-features.html#tf-idf")
  val category = TextProcessing

  lazy val tTagInfoE: Tag[IDFEstimatorInfo] = typeTag
  lazy val portO_1: Tag[IDFModelInfo] = typeTag

}

object IDFInfo extends IDFInfo