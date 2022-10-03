package com.harana.sdk.backend.models.flow.actiontypes.spark.wrappers.transformers

import com.harana.sdk.backend.models.flow.actionobjects.spark.wrappers.transformers.PolynomialExpander
import com.harana.sdk.backend.models.flow.actiontypes.TransformerAsActionType
import com.harana.sdk.backend.models.flow.actionobjects.spark.wrappers.transformers.PolynomialExpander
import com.harana.sdk.shared.models.flow.actiontypes.spark.wrappers.transformers.PolynomialExpandInfo
import com.harana.sdk.shared.models.flow.documentation.SparkActionDocumentation

class PolynomialExpand extends TransformerAsActionType[PolynomialExpander]
  with PolynomialExpandInfo
  with SparkActionDocumentation