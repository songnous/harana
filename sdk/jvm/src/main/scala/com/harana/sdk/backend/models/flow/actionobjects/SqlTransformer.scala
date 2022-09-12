package com.harana.sdk.backend.models.flow.actionobjects

import com.harana.sdk.backend.models.flow.ExecutionContext
import com.harana.sdk.backend.models.flow.actionobjects.dataframe.DataFrame
import com.harana.sdk.backend.models.flow.actions.exceptions.SqlExpressionError
import com.harana.sdk.backend.models.flow.inference.{SqlInferenceWarning, SqlSchemaInferrer}
import com.harana.sdk.backend.models.flow.utils.Logging
import com.harana.sdk.shared.models.flow.actionobjects.SqlTransformerInfo
import com.harana.sdk.shared.models.flow.parameters.{CodeSnippetLanguage, CodeSnippetParameter, Parameter, StringParameter}
import com.harana.spark.SQL
import com.harana.spark.SparkSQLSession
import org.apache.spark.sql
import org.apache.spark.sql.types.StructType

class SqlTransformer extends Transformer
  with Logging
  with SqlTransformerInfo {

  def applyTransform(ctx: ExecutionContext, df: DataFrame) = {
    println(s"SqlExpression(expression = '$getExpression', dataFrameId = '$getDataFrameId')")

    val localSparkSQLSession = ctx.sparkSQLSession.newSession()
    val localDataFrame = moveToSparkSQLSession(df.sparkDataFrame, localSparkSQLSession)

    SQL.registerTempTable(localDataFrame, getDataFrameId)
    try {
      println(s"Table '${getDataFrameId}' registered. Executing the expression")
      val sqlResult = moveToSparkSQLSession(localSparkSQLSession.sql(getExpression), ctx.sparkSQLSession)
      DataFrame.fromSparkDataFrame(sqlResult)
    } finally {
      println("Unregistering the temporary table " + getDataFrameId)
      localSparkSQLSession.dropTempTable(getDataFrameId)
    }
  }

  override def applyTransformSchema(schema: StructType) = {
    val (resultSchema, warnings) = new SqlSchemaInferrer().inferSchema(getExpression, (getDataFrameId, schema))
    warnings.warnings.foreach {
      case SqlInferenceWarning(sqlExpression, warningText) => throw SqlExpressionError(sqlExpression, warningText).toException
      case other                                           => logger.warn(s"Inference warning not reported: ${other.message}")
    }
    Some(resultSchema)
  }

  private def moveToSparkSQLSession(df: sql.DataFrame, destinationSession: SparkSQLSession) = destinationSession.createDataFrame(df.rdd, df.schema)
}
