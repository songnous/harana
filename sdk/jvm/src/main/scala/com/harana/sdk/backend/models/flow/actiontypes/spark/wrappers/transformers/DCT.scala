package com.harana.sdk.backend.models.flow.actiontypes.spark.wrappers.transformers

import com.harana.sdk.backend.models.flow.actionobjects.spark.wrappers.transformers.DiscreteCosineTransformer
import com.harana.sdk.backend.models.flow.actiontypes.TransformerAsActionType
import com.harana.sdk.shared.models.common.Version
import com.harana.sdk.shared.models.flow.actiontypes.spark.wrappers.transformers.DCTInfo
import com.harana.sdk.shared.models.flow.documentation.SparkActionDocumentation
import com.harana.sdk.shared.models.flow.utils.Id

import izumi.reflect.Tag

class DCT extends TransformerAsActionType[DiscreteCosineTransformer]
  with DCTInfo
  with SparkActionDocumentation