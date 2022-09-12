package com.harana.sdk.backend.models.flow.actionobjects

import org.apache.spark.sql.Row
import org.apache.spark.sql.types.DataType
import org.apache.spark.sql.types.StructField
import org.apache.spark.sql.types.StructType
import com.harana.sdk.backend.models.flow.{ExecutionContext, IntegratedTestSupport}
import com.harana.sdk.shared.models.flow.exceptions.FlowError

trait MultiColumnTransformerTestSupport { self: IntegratedTestSupport =>

  import IntegratedTestSupport._

  def transformerName: String
  def transformer: MultiColumnTransformer

  def inputColumnName = "inputColumn"
  def outputColumnName = "outputColumn"

  def testValues: Seq[(Any, Any)]

  def inputType: DataType
  def outputType: DataType

  transformerName when {
    "transforming a column" should {

      "transform the selected column and save the results in output column" in {
        val t = transformer
        val out = t.transformSingleColumn(inputColumnName, outputColumnName, mock[ExecutionContext], supportInputDataFrame)
        assertDataFramesEqual(out, supportExpectedOutputDataFrame, checkNullability = false)
      }
    }

    "transforming schema" should {

      "return a schema that reflects changes done by transformation" in {
        val t = transformer
        val out = t.transformSingleColumnSchema(inputColumnName, outputColumnName, supportInputDataFrame.schema.get)
        out shouldBe 'Defined
        assertSchemaEqual(out.get, supportExpectedOutputDataFrame.schema.get, checkNullability = false)
      }

      "throw an exception" when {

        "output column already exists" in {
          val t = transformer
          a[FlowError] shouldBe thrownBy {
            t.transformSingleColumnSchema(
              inputColumnName,
              outputColumnName,
              supportDuplicatedColumnDataFrame.schema.get
            )
          }
        }

        "selected columns do not exist" in {
          val t = transformer
          an[FlowError] shouldBe thrownBy {
            t.transformSingleColumnSchema("columnThatDoesNotExist", outputColumnName, supportInputDataFrame.schema.get)
          }
        }
      }
    }
  }

  private lazy val (supportInputDataFrame, supportExpectedOutputDataFrame, supportDuplicatedColumnDataFrame) = {
    val inputValues = testValues.map(_._1)
    val outputValues = testValues.map(_._2)
    val inputColumn = inputValues
    val outputColumn = outputValues
    val anotherColumn = inputValues

    val inputData = inputColumn.zipWithIndex.map { case (v, idx) => Row(v, anotherColumn(idx)) }
    val outputData = inputColumn.zipWithIndex.map { case (v, idx) => Row(v, anotherColumn(idx), outputColumn(idx))}
    val duplicatedColumnData = inputData

    val inputSchema = StructType(
      Seq(
        StructField(inputColumnName, inputType),
        StructField("thirdColumn", inputType)
      )
    )

    val outputSchema = inputSchema.add(StructField(outputColumnName, outputType))

    val duplicatedColumnSchema = StructType(
      Seq(
        StructField(inputColumnName, inputType),
        StructField(outputColumnName, outputType),
        StructField("thirdColumn", inputType)
      )
    )

    val inputDataFrame = createDataFrame(inputData, inputSchema)
    val outputDataFrame = createDataFrame(outputData, outputSchema)
    val duplicatedColumnDataFrame = createDataFrame(duplicatedColumnData, duplicatedColumnSchema)

    (inputDataFrame, outputDataFrame, duplicatedColumnDataFrame)
  }
}
