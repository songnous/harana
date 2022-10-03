package com.harana.sdk.backend.models.flow.actionobjects

import com.harana.sdk.backend.models.flow.IntegratedTestSupport
import org.apache.spark.sql.Row
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.catalyst.analysis.NoSuchTableException
import org.apache.spark.sql.types._
import org.apache.spark.sql.functions._
import com.harana.sdk.backend.models.flow.actionobjects.dataframe.DataFrame
import com.harana.sdk.backend.models.flow.actionobjects.spark.wrappers.transformers.TransformerSerialization
import com.harana.sdk.backend.models.flow.actiontypes.exceptions.SqlExpressionError
import com.harana.spark.SQL
import com.harana.spark.spi.SparkSessionInitializer

class SqlTransformationIntegSpec extends IntegratedTestSupport with TransformerSerialization {

  import IntegratedTestSupport._
  import TransformerSerialization._

  val dataFrameId = "ThisIsAnId"
  val validExpression = s"select * from $dataFrameId"
  val invalidExpression = "foobar"
  val firstColumn = "firstColumn"
  val secondColumn = "secondColumn"
  val thirdColumn = "thirdColumn"

  val schema = StructType(
    Seq(
      StructField(firstColumn, StringType),
      StructField(secondColumn, DoubleType),
      StructField(thirdColumn, BooleanType)
    )
  )

  val data = Seq(
    Row("c", 5.0, true),
    Row("a", 5.0, null),
    Row("b", null, false),
    Row(null, 2.1, true)
  )

  "SqlTransformation" should {
    "allow to manipulate the input DataFrame using the specified name" in {
      val expression             = s"select $secondColumn from $dataFrameId"
      val result                 = executeSqlTransformation(expression, dataFrameId, sampleDataFrame)
      val selectedColumnsIndices = Seq(1) // 'secondColumn' index
      val expectedDataFrame      = subsetDataFrame(selectedColumnsIndices)
      assertDataFramesEqual(result, expectedDataFrame)
      assertTableUnregistered()
    }
    "allow use of UDFs defined in primary SQL Context" in {
      val expression        = s"select myOp($secondColumn) as foo from $dataFrameId"
      val resultSchema      = executeSqlSchemaTransformation(expression, dataFrameId, schema)
      val expectedSchema    = StructType(Seq(StructField("foo", IntegerType)))
      val result            = executeSqlTransformation(expression, dataFrameId, sampleDataFrame)
      val expectedDataFrame = sampleDataFrame.sparkDataFrame.select(col(secondColumn).cast(IntegerType).as("foo"))
      assertSchemaEqual(resultSchema, expectedSchema)

      assertDataFramesEqual(result, DataFrame.fromSparkDataFrame(expectedDataFrame))
      assertTableUnregistered()
    }
    "correctly infer output DataFrame schema" in {
      val expression             = s"select $firstColumn, $thirdColumn from $dataFrameId"
      val result                 = executeSqlSchemaTransformation(expression, dataFrameId, schema)
      val selectedColumnsIndices = Seq(0, 2)
      val expectedSchema         = StructType(selectedColumnsIndices.map(schema))
      assertSchemaEqual(result, expectedSchema)
      assertTableUnregistered()
    }
    "correctly infer schema when using user defined functions" in {
      val expression     = s"SELECT SIGNUM($secondColumn) sig FROM $dataFrameId"
      val result         = executeSqlSchemaTransformation(expression, dataFrameId, schema)
      val expectedSchema = StructType(Seq(StructField("sig", DoubleType)))
      assertSchemaEqual(result, expectedSchema)
    }
    "throw the appropriate exception when the table does not exist" in {
      val expression = s"SELECT * FROM notexistanttable"
      a[SqlExpressionError] should be thrownBy {
        executeSqlSchemaTransformation(expression, dataFrameId, schema)
      }
    }
    "throw the appropriate exception when trying to infer schema with invalid statement" in {
      val expression = s"SELEC * FRMO $dataFrameId"
      a[SqlExpressionError] should be thrownBy {
        executeSqlSchemaTransformation(expression, dataFrameId, schema)
      }
    }
    "throw the appropriate exception when no tables are used" in {
      val expression = s"SELECT *"
      a[SqlExpressionError] should be thrownBy {
        executeSqlSchemaTransformation(expression, dataFrameId, schema)
      }
    }
    "unregister the input DataFrame after execution" in {
      val dataFrame = sampleDataFrame
      executeSqlTransformation(validExpression, dataFrameId, dataFrame)
      assertTableUnregistered()
    }
    "unregister the input DataFrame if execution failed" in {
      val dataFrame = sampleDataFrame
      a[SQL.ExceptionThrownByInvalidExpression] should be thrownBy {
        executeSqlTransformation(invalidExpression, dataFrameId, dataFrame)
      }
      assertTableUnregistered()
    }
  }

  def assertTableUnregistered() = {
    intercept[NoSuchTableException] {
      executionContext.sparkSQLSession.table(dataFrameId)
    }
  }

  def executeSqlTransformation(expression: String, dataFrameId: String, input: DataFrame) = {
    val transformer = new SqlTransformer()
      .setExpression(expression)
      .setDataFrameId(dataFrameId)
    transformer.applyTransformationAndSerialization(tempDir, input)
  }

  def executeSqlSchemaTransformation(expression: String, dataFrameId: String, input: StructType): StructType = {
    val transformer = new SqlTransformer()
      .setExpression(expression)
      .setDataFrameId(dataFrameId)
    transformer._transformSchema(input).getOrElse(new StructType())
  }

  def sampleDataFrame = createDataFrame(data, schema)

  def subsetDataFrame(columns: Seq[Int]) = {
    val subSchema = StructType(columns.map(schema))
    val subData   = data.map(r => Row(columns.map(r.get): _*))
    createDataFrame(subData, subSchema)
  }
}
