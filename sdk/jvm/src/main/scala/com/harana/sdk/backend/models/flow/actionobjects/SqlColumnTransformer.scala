package com.harana.sdk.backend.models.flow.actionobjects

import com.harana.sdk.backend.models.flow.ExecutionContext
import com.harana.sdk.backend.models.flow.actionobjects.dataframe.DataFrame
import com.harana.sdk.backend.models.flow.actions.exceptions.{ColumnAliasNotUniqueError, ColumnsDoNotExistError, SqlColumnExpressionSyntaxError, SqlColumnTransformationExecutionError}
import com.harana.sdk.backend.models.flow.utils.{SparkTypeConverter, SparkUtils}
import com.harana.sdk.shared.models.flow.actionobjects.SqlColumnTransformerInfo
import com.harana.sdk.shared.models.flow.exceptions.FlowError
import com.harana.sdk.shared.models.flow.parameters.selections.NameColumnSelection
import com.harana.spark.SQL
import org.apache.spark.sql.types.StructType

class SqlColumnTransformer extends MultiColumnTransformer with SqlColumnTransformerInfo {

  def transformSingleColumn(inputColumn: String, outputColumn: String, ec: ExecutionContext, df: DataFrame) = {
    val inputColumnAlias = SparkUtils.escapeColumnName(getInputColumnAlias)
    val formula = getFormula
    val inputColumnName = SparkUtils.escapeColumnName(inputColumn)
    val outputColumnName = SparkUtils.escapeColumnName(outputColumn)

    val dataFrameSchema = df.sparkDataFrame.schema
    validate(dataFrameSchema)

    val (transformedSparkDataFrame, schema) =
      try {
        val inputColumnNames  = dataFrameSchema.map(c => SparkUtils.escapeColumnName(c.name))
        val outputColumnNames = inputColumnNames :+ s"$formula AS $outputColumnName"

        val outputDataFrame = df.sparkDataFrame.selectExpr("*", s"$inputColumnName AS $inputColumnAlias").selectExpr(outputColumnNames: _*)
        val schema = StructType(outputDataFrame.schema.map(_.copy(nullable = true)))

        (outputDataFrame, schema)
      } catch {
        case e: Exception =>
          throw SqlColumnTransformationExecutionError(inputColumnName, formula, outputColumnName, Some(e)).toException
      }

    ec.dataFrameBuilder.buildDataFrame(schema, transformedSparkDataFrame.rdd)
  }

  def transformSingleColumnSchema(inputColumn: String, outputColumn: String, schema: StructType): Option[StructType] = {
    validate(schema)
    None
  }

  private def validate(schema: StructType) = {
    validateFormula(schema)
    validateUniqueAlias(schema)
  }

  private def validateFormula(schema: StructType) = {
    val formula = getFormula
    try {
      val expression = SQL.SqlParser.parseExpression(formula)
      val columnNames = schema.map(_.name).toSet + getInputColumnAlias
      val referredColumnNames = expression.references.map(_.name).toSet
      if (!referredColumnNames.subsetOf(columnNames)) {
        val nonExistingColumns = referredColumnNames -- columnNames
        throw ColumnsDoNotExistError(NameColumnSelection(nonExistingColumns), SparkTypeConverter.fromSparkStructType(schema)).toException
      }
    } catch {
      case de: FlowError => throw de
      case e: Exception => throw SqlColumnExpressionSyntaxError(formula).toException
    }
  }

  private def validateUniqueAlias(schema: StructType) = {
    val alias = getInputColumnAlias
    if (schema.map(_.name).contains(alias)) throw ColumnAliasNotUniqueError(alias).toException
  }
}
