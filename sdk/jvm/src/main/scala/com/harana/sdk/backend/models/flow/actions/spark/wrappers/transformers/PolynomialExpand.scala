package com.harana.sdk.backend.models.flow.actions.spark.wrappers.transformers

import com.harana.sdk.backend.models.designer.flow.actionobjects.spark.wrappers.transformers.PolynomialExpander
import com.harana.sdk.backend.models.designer.flow.actions.TransformerAsAction
import com.harana.sdk.backend.models.flow.actionobjects.spark.wrappers.transformers.PolynomialExpander
import com.harana.sdk.shared.models.flow.actions.spark.wrappers.transformers.PolynomialExpandInfo
import com.harana.sdk.shared.models.flow.documentation.SparkActionDocumentation

class PolynomialExpand extends TransformerAsAction[PolynomialExpander]
  with PolynomialExpandInfo
  with SparkActionDocumentation