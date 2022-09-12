package com.harana.sdk.shared.models.flow.actions.spark.wrappers.transformers

import com.harana.sdk.shared.models.common.Version
import com.harana.sdk.shared.models.flow.catalogs.ActionCategory.Transformation
import com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.transformers.BinarizerInfo
import com.harana.sdk.shared.models.flow.actions.TransformerAsActionInfo
import com.harana.sdk.shared.models.flow.catalogs.ActionCategory.Transformation.FeatureConversion
import com.harana.sdk.shared.models.flow.documentation.SparkActionDocumentation
import com.harana.sdk.shared.models.flow.utils.Id

import scala.reflect.runtime.universe.TypeTag

trait BinarizeInfo extends TransformerAsActionInfo[BinarizerInfo] with SparkActionDocumentation {

  val id: Id = "c29f2401-0891-4223-8a33-41ecbe316de6"
  val name = "Binarize"
  val description = "Binarizes continuous features"
  val since = Version(1,0,0)
  val docsGuideLocation = Some("ml-features.html#binarizer")
  val category = FeatureConversion


  lazy val portO_1: TypeTag[BinarizerInfo] = typeTag

}

object BinarizeInfo extends BinarizeInfo