package com.harana.sdk.backend.models.flow.actionobjects

import com.harana.sdk.backend.models.flow.ExecutionContext
import com.harana.sdk.backend.models.flow.actionobjects.dataframe.DataFrame
import com.harana.sdk.shared.models.flow.actionobjects.RowsFiltererInfo
import com.harana.sdk.shared.models.flow.parameters.{CodeSnippetLanguage, CodeSnippetParameter, Parameter}
import com.harana.spark.SQL
import org.apache.spark.sql.types.StructType

class RowsFilterer extends Transformer with RowsFiltererInfo {

  def applyTransform(ctx: ExecutionContext, df: DataFrame) = {
    val uniqueDataFrameId   = "row_filterer_" + java.util.UUID.randomUUID.toString.replace('-', '_')
    val resultantExpression = s"SELECT * FROM $uniqueDataFrameId WHERE $getCondition"
    println("RowsFilterer(expression = 'resultantExpression', uniqueDataFrameId = '$uniqueDataFrameId')")

    SQL.registerTempTable(df.sparkDataFrame, uniqueDataFrameId)
    try {
      println(s"Table '$uniqueDataFrameId' registered. Executing the expression")
      val sqlResult = SQL.sparkSQLSession(df.sparkDataFrame).sql(resultantExpression)
      DataFrame.fromSparkDataFrame(sqlResult)
    } finally {
      println(s"Unregistering the temporary table '$uniqueDataFrameId'")
      SQL.sparkSQLSession(df.sparkDataFrame).dropTempTable(uniqueDataFrameId)
    }
  }

  override def applyTransformSchema(schema: StructType) = Some(schema)
}