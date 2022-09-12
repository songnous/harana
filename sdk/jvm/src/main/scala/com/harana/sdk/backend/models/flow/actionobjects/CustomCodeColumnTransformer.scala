package com.harana.sdk.backend.models.flow.actionobjects

import com.harana.sdk.backend.models.flow.ActionExecutionDispatcher.Result
import com.harana.sdk.backend.models.flow.ExecutionContext
import com.harana.sdk.backend.models.flow.utils.SparkTypeConverter.toSparkDataType
import com.harana.sdk.backend.models.flow.actionobjects.dataframe.DataFrame
import com.harana.sdk.backend.models.flow.actions.exceptions.CustomActionExecutionError
import com.harana.sdk.backend.models.flow.utils.SparkTypeConverter
import com.harana.sdk.shared.models.flow.actionobjects.CustomCodeColumnTransformerInfo
import com.harana.sdk.shared.models.flow.parameters.{CodeSnippetParameter, Parameter}
import com.harana.sdk.shared.models.flow.parameters.choice.ChoiceParameter
import org.apache.spark.sql.types.DataType
import org.apache.spark.sql.types.StructField
import org.apache.spark.sql.types.StructType

abstract class CustomCodeColumnTransformer() extends MultiColumnTransformer with CustomCodeColumnTransformerInfo {
  import CustomCodeColumnTransformerInfo._

  def runCode(context: ExecutionContext, code: String): Result

  def isValid(context: ExecutionContext, code: String): Boolean

  def getComposedCode(userCode: String, inputColumn: String, outputColumn: String, targetType: DataType): String

  private def executeCode(code: String, inputColumn: String, outputColumn: String, context: ExecutionContext, dataFrame: DataFrame) =
    runCode(context, code) match {
      case Left(error) =>
        throw CustomActionExecutionError(s"Execution exception:\n\n$error").toException

      case Right(_) =>
        val sparkDataFrame = context.dataFrameStorage.getOutputDataFrame(OutputPortNumber).getOrElse {
          throw CustomActionExecutionError("action finished successfully, but did not produce a DataFrame.").toException
        }

        val newSparkDataFrame = context.sparkSQLSession.createDataFrame(sparkDataFrame.rdd,
          transformSingleColumnSchema(inputColumn, outputColumn, dataFrame.schema.get).get
        )
        DataFrame.fromSparkDataFrame(newSparkDataFrame)
    }

  def transformSingleColumn(inputColumn: String, outputColumn: String, context: ExecutionContext, dataFrame: DataFrame) = {
    val code = getComposedCode($(codeParameter), inputColumn, outputColumn, toSparkDataType(getTargetType.columnType))
    println(s"Code to be validated and executed:\n$code")

    if (!isValid(context, code)) throw CustomActionExecutionError("Code validation failed").toException

    context.dataFrameStorage.withInputDataFrame(InputPortNumber, dataFrame.sparkDataFrame) {
      executeCode(code, inputColumn, outputColumn, context, dataFrame)
    }
  }

  def transformSingleColumnSchema(inputColumn: String, outputColumn: String, schema: StructType): Option[StructType] = {
    MultiColumnTransformer.assertColumnExist(inputColumn, schema)
    MultiColumnTransformer.assertColumnDoesNotExist(outputColumn, schema)
    Some(schema.add(StructField(outputColumn, toSparkDataType(getTargetType.columnType), nullable = true)))
  }
}