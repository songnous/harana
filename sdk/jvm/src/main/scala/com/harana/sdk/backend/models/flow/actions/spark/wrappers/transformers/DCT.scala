package com.harana.sdk.backend.models.flow.actions.spark.wrappers.transformers

import com.harana.sdk.backend.models.flow.actionobjects.spark.wrappers.transformers.DiscreteCosineTransformer
import com.harana.sdk.backend.models.flow.actions.TransformerAsAction
import com.harana.sdk.shared.models.common.Version
import com.harana.sdk.shared.models.flow.actions.spark.wrappers.transformers.DCTInfo
import com.harana.sdk.shared.models.flow.documentation.SparkActionDocumentation
import com.harana.sdk.shared.models.flow.utils.Id

import scala.reflect.runtime.universe.TypeTag

class DCT extends TransformerAsAction[DiscreteCosineTransformer]
  with DCTInfo
  with SparkActionDocumentation