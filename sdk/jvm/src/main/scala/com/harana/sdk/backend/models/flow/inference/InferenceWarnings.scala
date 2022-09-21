package com.harana.sdk.backend.models.flow.inference

import io.circe.generic.JsonCodec

@JsonCodec
case class InferenceWarnings(warnings: List[InferenceWarning]) {
  def :+(warning: InferenceWarning): InferenceWarnings = InferenceWarnings(warnings :+ warning)
  def ++(other: InferenceWarnings): InferenceWarnings = InferenceWarnings(warnings ++ other.warnings)
  def isEmpty: Boolean = warnings.isEmpty
}

object InferenceWarnings {
  def empty = InferenceWarnings(List.empty[InferenceWarning])
  def apply(warnings: InferenceWarning*): InferenceWarnings = InferenceWarnings(warnings.toList)
  def flatten(inferenceWarnings: Iterable[InferenceWarnings]) = InferenceWarnings(inferenceWarnings.flatMap(_.warnings).toList)
}
