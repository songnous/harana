package com.harana.sdk.backend.models.flow.actions.spark.wrappers.transformers

import com.harana.sdk.backend.models.flow.actionobjects.spark.wrappers.transformers.RegexTokenizer
import com.harana.sdk.backend.models.flow.actions.TransformerAsAction
import com.harana.sdk.shared.models.flow.actions.spark.wrappers.transformers.TokenizeWithRegexInfo
import com.harana.sdk.shared.models.flow.documentation.SparkActionDocumentation

class TokenizeWithRegex extends TransformerAsAction[RegexTokenizer]
  with TokenizeWithRegexInfo
  with SparkActionDocumentation