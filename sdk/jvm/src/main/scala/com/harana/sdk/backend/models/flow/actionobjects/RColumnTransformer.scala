package com.harana.sdk.backend.models.flow.actionobjects

import com.harana.sdk.backend.models.flow.ExecutionContext
import com.harana.sdk.shared.models.flow.actionobjects.RColumnTransformerInfo
import org.apache.spark.sql.types.DataType

import java.util.UUID

class RColumnTransformer() extends CustomCodeColumnTransformer with RColumnTransformerInfo {

  def getComposedCode(userCode: String, inputColumn: String, outputColumn: String, targetType: DataType) = {
    val newFieldName = UUID.randomUUID().toString.replace("-", "")

    s"""
       |$userCode
       |
       |transform <- function(dataframe) {
       |  new.column <- cast(transform.column(dataframe$$'$inputColumn', '$inputColumn'),
       |    '${targetType.simpleString}')
       |  return(withColumn(dataframe, '$newFieldName', new.column))
       |}
    """.stripMargin
  }

  def runCode(context: ExecutionContext, code: String) =
    context.customCodeExecutor.runR(code)

  def isValid(context: ExecutionContext, code: String): Boolean =
    context.customCodeExecutor.isRValid(code)
}
