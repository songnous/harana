package com.harana.sdk.backend.models.flow.actiontypes

import com.harana.sdk.backend.models.flow.Knowledge
import com.harana.sdk.backend.models.flow.actionobjects.dataframe.DataFrame
import com.harana.sdk.backend.models.flow.inference.{InferContext, InferenceWarnings}
import org.apache.spark.sql.types.StructType

trait DataFrame2To1Action {
  self: ActionTypeType2To1[DataFrame, DataFrame, DataFrame] =>

  final override def inferKnowledge(leftDataFrameKnowledge: Knowledge[DataFrame], rightDataFrameKnowledge: Knowledge[DataFrame])(context: InferContext) = {
    val leftSchema = leftDataFrameKnowledge.single.schema
    val rightSchema = rightDataFrameKnowledge.single.schema

    if (leftSchema.nonEmpty && rightSchema.nonEmpty) {
      val (outputSchema, warnings) = inferSchema(leftSchema.get, rightSchema.get)
      (Knowledge(DataFrame.forInference(outputSchema)), warnings)
    } else
      (Knowledge(DataFrame.forInference()), InferenceWarnings.empty)
  }

  def inferSchema(leftSchema: StructType, rightSchema: StructType) =
    (StructType(Seq.empty), InferenceWarnings.empty)

}
