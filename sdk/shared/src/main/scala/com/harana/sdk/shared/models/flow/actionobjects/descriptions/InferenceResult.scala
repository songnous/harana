package com.harana.sdk.shared.models.flow.actionobjects.descriptions

import com.harana.sdk.shared.models.flow.StructType
import io.circe.Json

sealed trait InferenceResult

case class DataFrameInferenceResult(schema: StructType) extends InferenceResult
case class ParametersInferenceResult(schema: Json, values: Json) extends InferenceResult