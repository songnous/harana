package com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.transformers

import com.harana.sdk.shared.models.flow.actionobjects.TransformerInfo
import com.harana.sdk.shared.models.flow.parameters.{BooleanParameter, Parameter, Parameters}

trait StopWordsRemoverInfo extends TransformerInfo with Parameters {

  val id = "D791C2E7-1843-490F-A672-67B2F02FFB79"

  val caseSensitiveParameter = BooleanParameter("case sensitive")
  setDefault(caseSensitiveParameter, false)

  val parameters = Left(Array.empty[Parameter[_]])
  val specificParameters = Array[Parameter[_]](caseSensitiveParameter)

}

object StopWordsRemoverInfo extends StopWordsRemoverInfo