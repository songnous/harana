package com.harana.sdk.shared.models.flow.actiontypes.spark.wrappers.transformers

import com.harana.sdk.shared.models.common.Version
import com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.transformers.VectorAssemblerInfo
import com.harana.sdk.shared.models.flow.actiontypes.TransformerAsActionInfo
import com.harana.sdk.shared.models.flow.catalogs.ActionCategory.Transformation.FeatureConversion
import com.harana.sdk.shared.models.flow.documentation.SparkActionDocumentation
import com.harana.sdk.shared.models.flow.utils.Id

import izumi.reflect.Tag

trait AssembleVectorInfo extends TransformerAsActionInfo[VectorAssemblerInfo] with SparkActionDocumentation {

  val id: Id = "c57a5b99-9184-4095-9037-9359f905628d"
  val name = "assemble-vector"
  val since = Version(1,0,0)
  val docsGuideLocation = Some("ml-features.html#vectorassembler")
  val category = FeatureConversion


  lazy val portO_1: Tag[VectorAssemblerInfo] = typeTag

}

object AssembleVectorInfo extends AssembleVectorInfo