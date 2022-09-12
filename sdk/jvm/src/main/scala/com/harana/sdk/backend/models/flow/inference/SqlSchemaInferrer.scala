package com.harana.sdk.backend.models.flow.inference

import com.harana.spark.SQL
import io.circe.generic.JsonCodec
import org.apache.spark.sql.{AnalysisException, Row}
import org.apache.spark.sql.types.StructType

@JsonCodec
case class SqlInferenceWarning(sqlExpression: String, warningText: String) extends InferenceWarning {
  val message = s"Schema for SQL formula '$sqlExpression' cannot be inferred ($warningText)."
}

class SqlSchemaInferrer {

  def inferSchema(sqlExpression: String, inputSchemas: (String, StructType)*): (StructType, InferenceWarnings) = {
    try {
      val localSpark = SQL.createEmptySparkSQLSession()
      inputSchemas.foreach { case (dataFrameId, schema) =>
        val emptyData = localSpark.sparkContext.parallelize(Seq(Row.empty))
        val emptyDf = localSpark.createDataFrame(emptyData, schema)
        SQL.registerTempTable(emptyDf, dataFrameId)
      }
      val resultSchema = localSpark.sql(sqlExpression).schema
      val warnings =
        if (!namesUnique(inputSchemas))
          InferenceWarnings(SqlInferenceWarning(sqlExpression, "DataFrame ids must be unique."))
        else if (resultSchema.isEmpty)
          InferenceWarnings(SqlInferenceWarning(sqlExpression, "Expression must be non-empty."))
        else
          InferenceWarnings.empty
      (resultSchema, warnings)
    } catch {
      case e @ (_: AnalysisException | _: IllegalArgumentException) =>
        (StructType(Seq.empty), InferenceWarnings(SqlInferenceWarning(sqlExpression, s"Invalid Spark SQL expression: ${e.getMessage}")))
    }
  }

  private def namesUnique(inputSchemas: Seq[(String, StructType)]) = {
    val names = inputSchemas.map { case (name, _) => name }
    names.size == names.toSet.size
  }
}