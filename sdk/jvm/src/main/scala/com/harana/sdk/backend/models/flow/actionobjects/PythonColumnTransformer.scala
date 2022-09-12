package com.harana.sdk.backend.models.flow.actionobjects

import com.harana.sdk.backend.models.designer.flow._
import com.harana.sdk.backend.models.flow.ExecutionContext
import com.harana.sdk.shared.models.flow.actionobjects.PythonColumnTransformerInfo
import com.harana.sdk.shared.models.flow.parameters.{CodeSnippetLanguage, CodeSnippetParameter}
import org.apache.spark.sql.types._

import java.util.UUID

class PythonColumnTransformer extends CustomCodeColumnTransformer with PythonColumnTransformerInfo {

  def runCode(context: ExecutionContext, code: String) = context.customCodeExecutor.runPython(code)

  def isValid(context: ExecutionContext, code: String) = context.customCodeExecutor.isPythonValid(code)

  def getComposedCode(userCode: String, inputColumn: String, outputColumn: String, targetType: DataType) = {
    val newFieldName = UUID.randomUUID().toString.replace("-", "")
    val newFieldJson = s"""{"name": "$newFieldName", "type":${targetType.toString}, "nullable":true, "metadata":null}"""

    s"""
       |$userCode
       |
       |from pyspark.sql.types import *
       |import json
       |
       |def transform(dataframe):
       |    new_field = StructField.fromJson(json.loads(\"\"\"$newFieldJson\"\"\"))
       |    schema = StructType(dataframe.schema.fields + [new_field])
       |    def _transform_row(row):
       |        return row + (transform_value(row['$inputColumn'], '$inputColumn'))
       |    return spark.createDataFrame(dataframe.rdd.map(_transform_row), schema)
    """.stripMargin
  }
}
