package com.harana.sdk.backend.models.flow.actionobjects.spark.wrappers.transformers

import com.harana.sdk.backend.models.flow.actionobjects.SparkTransformerWrapper
import com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.transformers.VectorAssemblerInfo
import com.harana.sdk.shared.models.flow.parameters.selections.{MultipleColumnSelection, NameColumnSelection}
import com.harana.sdk.shared.models.flow.parameters.{ColumnSelectorParameter, SingleColumnCreatorParameter}
import org.apache.spark.ml.feature.{VectorAssembler => SparkVectorAssembler}

class VectorAssembler
  extends SparkTransformerWrapper[SparkVectorAssembler]
    with VectorAssemblerInfo