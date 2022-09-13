package com.harana.sdk.shared.models.flow.actions.spark.wrappers.transformers

import com.harana.sdk.shared.models.common.Version
import com.harana.sdk.shared.models.flow.catalogs.ActionCategory.Transformation
import com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.transformers.NGramTransformerInfo
import com.harana.sdk.shared.models.flow.actions.TransformerAsActionInfo
import com.harana.sdk.shared.models.flow.catalogs.ActionCategory.Transformation.TextProcessing
import com.harana.sdk.shared.models.flow.documentation.SparkActionDocumentation
import com.harana.sdk.shared.models.flow.utils.Id

import scala.reflect.runtime.universe.TypeTag

trait ConvertToNGramsInfo extends TransformerAsActionInfo[NGramTransformerInfo] with SparkActionDocumentation {

  val id: Id = "06a73bfe-4e1a-4cde-ae6c-ad5a31f72496"
  val name = "Convert To n-grams"
  val description = "Converts arrays of strings to arrays of n-grams. Null " +
    "values in the input arrays are ignored. Each n-gram is represented by a space-separated " +
    "string of words. When the input is empty, an empty array is returned. When the input array " +
    "is shorter than n (number of elements per n-gram), no n-grams are returned."
  val since = Version(1,0,0)
  val docsGuideLocation = Some("ml-features.html#n-gram")
  val category = TextProcessing

  lazy val portO_1: TypeTag[NGramTransformerInfo] = typeTag

}

object ConvertToNGramsInfo extends ConvertToNGramsInfo