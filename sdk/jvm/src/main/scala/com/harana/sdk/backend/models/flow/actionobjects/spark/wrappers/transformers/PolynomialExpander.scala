package com.harana.sdk.backend.models.flow.actionobjects.spark.wrappers.transformers

import com.harana.sdk.backend.models.flow.actionobjects.SparkTransformerAsMultiColumnTransformer
import com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.transformers.PolynomialExpanderInfo
import com.harana.sdk.shared.models.flow.IntParameter
import com.harana.sdk.shared.models.flow.parameters.validators.RangeValidator
import org.apache.spark.ml.feature.PolynomialExpansion

class PolynomialExpander extends SparkTransformerAsMultiColumnTransformer[PolynomialExpansion] with PolynomialExpanderInfo {

  override def convertInputNumericToVector = true
  override def convertOutputVectorToDouble = false

}
