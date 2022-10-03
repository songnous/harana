package com.harana.sdk.backend.models.flow.actiontypes

import com.harana.sdk.backend.models.flow.ExecutionContext
import com.harana.sdk.backend.models.flow.actionobjects.dataframe.DataFrame
import com.harana.sdk.backend.models.flow.inference.SqlSchemaInferrer
import com.harana.sdk.shared.models.flow.actiontypes.SqlCombineInfo
import com.harana.spark.{SQL, SparkSQLSession}
import org.apache.spark.sql
import org.apache.spark.sql.types.StructType

import scala.reflect.runtime.universe.TypeTag

class SqlCombine extends ActionTypeType2To1[DataFrame, DataFrame, DataFrame]
  with SqlCombineInfo
  with DataFrame2To1Action {

  def execute(left: DataFrame, right: DataFrame)(ctx: ExecutionContext) = {
    println(s"SqlCombine(expression = '$getSqlCombineExpression', leftTableName = '$getLeftTableName', rightTableName = '$getRightTableName')")
    val localSparkSQLSession = ctx.sparkSQLSession.newSession()
    val leftDf = moveToSparkSQLSession(left.sparkDataFrame, localSparkSQLSession)
    val rightDf = moveToSparkSQLSession(right.sparkDataFrame, localSparkSQLSession)

    SQL.registerTempTable(leftDf, getLeftTableName)
    SQL.registerTempTable(rightDf, getRightTableName)
    println(s"Tables '$getLeftTableName', '$getRightTableName' registered. Executing the expression")
    val localSqlResult = localSparkSQLSession.sql(getSqlCombineExpression)
    val sqlResult = moveToSparkSQLSession(localSqlResult, ctx.sparkSQLSession)
    DataFrame.fromSparkDataFrame(sqlResult)
  }

  override def inferSchema(leftSchema: StructType, rightSchema: StructType) =
    new SqlSchemaInferrer().inferSchema(getSqlCombineExpression, (getLeftTableName, leftSchema), (getRightTableName, rightSchema))

  def moveToSparkSQLSession(df: sql.DataFrame, destinationCtx: SparkSQLSession) =
    destinationCtx.createDataFrame(df.rdd, df.schema)

  lazy val tTagTO_0: TypeTag[DataFrame] = typeTag

}