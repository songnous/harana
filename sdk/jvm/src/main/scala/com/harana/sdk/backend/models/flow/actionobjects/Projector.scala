package com.harana.sdk.backend.models.flow.actionobjects

import com.harana.sdk.backend.models.flow.ExecutionContext
import com.harana.sdk.backend.models.flow.actionobjects.dataframe.{DataFrame, DataFrameColumnsGetter}
import com.harana.sdk.backend.models.flow.utils.SparkUtils
import com.harana.sdk.shared.models.flow.actionobjects.ProjectorInfo
import org.apache.spark.sql.types.StructType

class Projector extends Transformer with ProjectorInfo {

  def applyTransform(ctx: ExecutionContext, df: DataFrame) = {
    val exprSeq = getProjectionColumns.map { cp =>
      val renameExpressionPart = cp.getRenameColumn.getColumnName match {
        case None => ""
        case Some(columnName) => s" AS ${SparkUtils.escapeColumnName(columnName)}"
      }
      SparkUtils.escapeColumnName(df.getColumnName(cp.getOriginalColumn)) + renameExpressionPart
    }
    if (exprSeq.isEmpty) DataFrame.empty(ctx) else DataFrame.fromSparkDataFrame(df.sparkDataFrame.selectExpr(exprSeq: _*))
  }

  override def applyTransformSchema(schema: StructType): Option[StructType] = {
    val namesPairsSeq = getProjectionColumns.map { cp =>
      val originalColumnName = DataFrameColumnsGetter.getColumnName(schema, cp.getOriginalColumn)
      val resultColumnName = cp.getRenameColumn.getColumnName match {
        case None => originalColumnName
        case Some(columnName) => columnName
      }
      (originalColumnName, resultColumnName)
    }
    val fields = namesPairsSeq.map { case (originalColumnName: String, renamedColumnName: String) =>
      schema(originalColumnName).copy(name = renamedColumnName)
    }
    Some(StructType(fields))
  }
}