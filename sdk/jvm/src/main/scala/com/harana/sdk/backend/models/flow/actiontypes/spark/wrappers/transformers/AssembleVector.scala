package com.harana.sdk.backend.models.flow.actiontypes.spark.wrappers.transformers

import com.harana.sdk.backend.models.flow.actionobjects.spark.wrappers.transformers.VectorAssembler
import com.harana.sdk.backend.models.flow.actiontypes.TransformerAsActionType
import com.harana.sdk.backend.models.flow.actionobjects.spark.wrappers.transformers.VectorAssembler
import com.harana.sdk.shared.models.common.Version
import com.harana.sdk.shared.models.flow.actiontypes.spark.wrappers.transformers.AssembleVectorInfo
import com.harana.sdk.shared.models.flow.documentation.SparkActionDocumentation
import com.harana.sdk.shared.models.flow.utils.Id

import scala.reflect.runtime.universe.TypeTag

class AssembleVector extends TransformerAsActionType[VectorAssembler]
  with AssembleVectorInfo
  with SparkActionDocumentation