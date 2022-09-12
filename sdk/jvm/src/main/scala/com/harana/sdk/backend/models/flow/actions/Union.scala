package com.harana.sdk.backend.models.flow.actions

import com.harana.sdk.backend.models.flow.{Action2To1, DataFrame2To1Action, ExecutionContext}
import com.harana.sdk.backend.models.flow.actionobjects.dataframe.DataFrame
import com.harana.sdk.backend.models.flow.actions.exceptions.SchemaMismatchError
import com.harana.sdk.backend.models.flow.inference.InferenceWarnings
import com.harana.sdk.shared.models.flow.actionobjects.RTransformerInfo
import com.harana.sdk.shared.models.flow.actions.UnionInfo
import com.harana.sdk.shared.models.flow.parameters.Parameters
import com.harana.spark.SQL
import org.apache.spark.sql.types.StructType

import scala.reflect.runtime.universe.TypeTag

class Union extends Action2To1[DataFrame, DataFrame, DataFrame]
  with UnionInfo
  with DataFrame2To1Action
  with Parameters {

  def execute(first: DataFrame, second: DataFrame)(context: ExecutionContext) = {
    inferSchema(first.schema.get, second.schema.get)
    context.dataFrameBuilder.buildDataFrame(first.schema.get, SQL.union(first.sparkDataFrame, second.sparkDataFrame).rdd)
  }

  override def inferSchema(leftSchema: StructType, rightSchema: StructType): (StructType, InferenceWarnings) = {
    if (leftSchema.treeString != rightSchema.treeString)
      throw SchemaMismatchError(s"SchemaMismatch. Expected schema ${leftSchema.treeString} differs from ${rightSchema.treeString}").toException
    (leftSchema, InferenceWarnings.empty)
  }

  lazy val tTagTO_0: TypeTag[DataFrame] = typeTag

}