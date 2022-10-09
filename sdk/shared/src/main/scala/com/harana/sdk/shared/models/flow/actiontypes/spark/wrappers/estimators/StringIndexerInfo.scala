package com.harana.sdk.shared.models.flow.actiontypes.spark.wrappers.estimators

import com.harana.sdk.shared.models.common.Version
import com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.estimators.StringIndexerEstimatorInfo
import com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.models.StringIndexerModelInfo
import com.harana.sdk.shared.models.flow.actiontypes.EstimatorAsActionInfo
import com.harana.sdk.shared.models.flow.catalogs.ActionCategory.Transformation.FeatureConversion
import com.harana.sdk.shared.models.flow.documentation.SparkActionDocumentation
import com.harana.sdk.shared.models.flow.utils.Id

import izumi.reflect.Tag

trait StringIndexerInfo
    extends EstimatorAsActionInfo[StringIndexerEstimatorInfo, StringIndexerModelInfo]
    with SparkActionDocumentation {

  val id: Id = "c9df7000-9ea0-41c0-b66c-3062fd57851b"
  val name = "string-indexer"
  val since = Version(1,0,0)
  val docsGuideLocation = Some("ml-features.html#stringindexer")
  val category = FeatureConversion

  lazy val tTagInfoE: Tag[StringIndexerEstimatorInfo] = typeTag
  lazy val portO_1: Tag[StringIndexerModelInfo] = typeTag

}

object StringIndexerInfo extends StringIndexerInfo