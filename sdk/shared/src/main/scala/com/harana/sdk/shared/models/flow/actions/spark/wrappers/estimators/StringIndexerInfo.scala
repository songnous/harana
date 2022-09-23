package com.harana.sdk.shared.models.flow.actions.spark.wrappers.estimators

import com.harana.sdk.shared.models.common.Version
import com.harana.sdk.shared.models.flow.catalogs.ActionCategory.Transformation
import com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.estimators.StringIndexerEstimatorInfo
import com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.models.{StringIndexerModelInfo, UnivariateFeatureModelInfo}
import com.harana.sdk.shared.models.flow.actions.EstimatorAsActionInfo
import com.harana.sdk.shared.models.flow.catalogs.ActionCategory.Transformation.FeatureConversion
import com.harana.sdk.shared.models.flow.documentation.SparkActionDocumentation
import com.harana.sdk.shared.models.flow.utils.Id

import scala.reflect.runtime.universe.TypeTag

trait StringIndexerInfo
    extends EstimatorAsActionInfo[StringIndexerEstimatorInfo, StringIndexerModelInfo]
    with SparkActionDocumentation {

  val id: Id = "c9df7000-9ea0-41c0-b66c-3062fd57851b"
  val name = "String Indexer"
  val since = Version(1,0,0)
  val docsGuideLocation = Some("ml-features.html#stringindexer")
  val category = FeatureConversion

  lazy val tTagInfoE: TypeTag[StringIndexerEstimatorInfo] = typeTag
  lazy val portO_1: TypeTag[StringIndexerModelInfo] = typeTag

}

object StringIndexerInfo extends StringIndexerInfo {
  def apply(pos: (Int, Int), color: Option[String] = None) = new StringIndexerInfo {
    override val position = Some(pos)
    override val overrideColor = color
  }
}