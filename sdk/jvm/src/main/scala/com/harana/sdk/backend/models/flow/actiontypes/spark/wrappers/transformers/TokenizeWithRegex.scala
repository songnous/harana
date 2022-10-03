package com.harana.sdk.backend.models.flow.actiontypes.spark.wrappers.transformers

import com.harana.sdk.backend.models.flow.actionobjects.spark.wrappers.transformers.RegexTokenizer
import com.harana.sdk.backend.models.flow.actiontypes.TransformerAsActionType
import com.harana.sdk.shared.models.flow.actiontypes.spark.wrappers.transformers.TokenizeWithRegexInfo
import com.harana.sdk.shared.models.flow.documentation.SparkActionDocumentation

class TokenizeWithRegex extends TransformerAsActionType[RegexTokenizer]
  with TokenizeWithRegexInfo
  with SparkActionDocumentation