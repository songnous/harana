package com.harana.sdk.backend.models.flow.actionobjects

import org.apache.spark.sql.Row
import org.apache.spark.sql.types._
import com.harana.sdk.shared.models.flow.actionobjects.multicolumn.MultiColumnParameters.SingleOrMultiColumnChoices.SingleColumnChoice
import com.harana.sdk.shared.models.flow.actionobjects.multicolumn.SingleColumnParameters.SingleTransformInPlaceChoices.NoInPlaceChoice
import com.harana.sdk.backend.models.flow.actiontypes.exceptions._
import com.harana.sdk.backend.models.flow.IntegratedTestSupport
import com.harana.sdk.backend.models.flow.actionobjects.dataframe.DataFrame
import com.harana.sdk.backend.models.flow.actionobjects.spark.wrappers.transformers.TransformerSerialization
import com.harana.sdk.backend.models.flow.actiontypes.exceptions.{ColumnAliasNotUniqueError, ColumnDoesNotExistError, ColumnsDoNotExistError, SqlColumnExpressionSyntaxError}
import com.harana.sdk.shared.models.flow.exceptions.ActionExecutionError
import com.harana.sdk.shared.models.flow.parameters.selections.NameSingleColumnSelection

class SqlColumnTransformationIntegSpec extends IntegratedTestSupport with TransformerSerialization {

  import TransformerSerialization._

  val delta = 0.01

  val resultColumn = 3
  val column0 = "c0"
  val column1 = "c1"
  val column2 = "c2"
  val column3 = "c3"

  val column3needsEscaping = "c.strange name!"

  "SqlColumnTransformation" should {

    "create Transformation that counts ABS properly" in {
      runTest("ABS(x)", column1, column3, Seq(1.0, 1.1, 1.2, 1.3, null))
    }

    "create Transformation that counts POW properly" in {
      runTest("POW(x, 2.0)", column1, column3, Seq(1.0, 1.21, 1.44, 1.69, null))
    }

    "create Transformation that counts SQRT properly" in {
      runTest("SQRT(x)", column2, column3, Seq(0.447, 1.483, null, 2.04, null))
    }

    "create Transformation that counts SIN properly" in {
      runTest("SIN(x)", column1, column3, Seq(0.841, -0.891, 0.932, -0.96, null))
    }

    "create Transformation that counts COS properly" in {
      runTest("COS(x)", column1, column3, Seq(0.540, 0.453, 0.362, 0.267, null))
    }

    "create Transformation that counts TAN properly" in {
      runTest("TAN(x)", column1, column3, Seq(1.557, -1.964, 2.572, -3.602, null))
    }

    "create Transformation that counts LN properly" in {
      runTest("LN(x)", column2, column3, Seq(-1.609, 0.788, null, 1.435, null))
    }

    "create Transformation that counts FLOOR properly" in {
      runTest("FLOOR(x)", column1, column3, Seq(1.0, -2.0, 1.0, -2.0, null), resultType = LongType)
    }

    "create Transformation that counts CEIL properly" in {
      runTest("CEIL(x)", column1, column3, Seq(1.0, -1.0, 2.0, -1.0, null), resultType = LongType)
    }

    "create Transformation that counts SIGNUM properly" in {
      runTest("SIGNUM(x)", column1, column3, Seq(1.0, -1.0, 1.0, -1.0, null))
    }
  }

  it should {
    "create Transformation that counts MIN properly" in {
      runTest(s"MIN($column1, $column2)", column1, column3, Seq(0.2, -1.1, null, -1.3, null))
    }

    "create Transformation that counts MAX properly" in {
      runTest(s"MAX($column1, $column2)", column1, column3, Seq(1.0, 2.2, null, 4.2, null))
    }

    "create Transformation that counts complex formulas properly" in {
      runTest(s"MAX(SIN($column2) + 1.0, ABS($column1 - 2.0))", column1, column3, Seq(1.19, 3.1, null, 3.3, null))
    }
  }

  it should {
    "detect missing inputColumn during inference" in {
      a[ColumnDoesNotExistError] shouldBe thrownBy {
        val inPlace = NoInPlaceChoice().setOutputColumn(column3)
        val single = SingleColumnChoice().setInputColumn(NameSingleColumnSelection("nonExistingCol")).setInPlaceChoice(inPlace)
        new SqlColumnTransformer().setFormula("x * 2").setSingleOrMultiChoice(single)._transformSchema(schema)
      }
    }
    val inPlace = NoInPlaceChoice().setOutputColumn(column3)
    val single = SingleColumnChoice().setInputColumn(NameSingleColumnSelection(column1)).setInPlaceChoice(inPlace)
    "detect SQL syntax error during inference" in {
      a[SqlColumnExpressionSyntaxError] shouldBe thrownBy(
        new SqlColumnTransformer().setFormula("+++---").setSingleOrMultiChoice(single)._transformSchema(schema)
      )
    }
    "detect non-existent column during inference" in {
      a[ColumnsDoNotExistError] shouldBe thrownBy(
        new SqlColumnTransformer().setFormula("nonExistingCol").setSingleOrMultiChoice(single)._transformSchema(schema)
      )
    }
    "detect that alias conflicts with a column name form input DF" in {
      a[ColumnAliasNotUniqueError] shouldBe thrownBy(
        new SqlColumnTransformer().setFormula("c0").setInputColumnAlias("c0").setSingleOrMultiChoice(single)._transformSchema(schema)
      )
    }
  }

  it should {
    "work with user-defined column alias" in {
      runTest("ABS(y)", column1, column3, Seq(1.0, 1.1, 1.2, 1.3, null), "y")
    }

    "create Transformation that produces properly escaped column name" in {
      val dataFrame = applyFormulaToDataFrame("COS(x)", column1, s"$column3needsEscaping", "x", prepareDataFrame())
      val rows = dataFrame.sparkDataFrame.collect()
      validateColumn(rows, Seq(0.540, 0.453, 0.362, 0.267, null))
      val schema = dataFrame.sparkDataFrame.schema
      schema.fieldNames shouldBe Array(column0, column1, column2, column3needsEscaping)
    }

    "create Transformation that works on DataFrame with backtics in column names".is(pending)

    // Regression test
    "create Transformation that works on DataFrame with non-standard column names" in {
      val column0 = "c 0"
      val column1 = "c-2"
      val column2 = "c - 2"

      def prepareDataFrame() = {
        val schema = StructType(List(StructField(column0, StringType), StructField(column1, DoubleType), StructField(column2, DoubleType)))
        val manualRowsSeq = Seq(
          Row("aaa", 1.0, 0.2),
          Row("bbb", -1.1, 2.2),
          Row("ccc", 1.2, null),
          Row("ddd", -1.3, 4.2),
          Row("eee", null, null)
        )
        createDataFrame(manualRowsSeq, schema)
      }
      val dataFrame = applyFormulaToDataFrame("COS(x)", column1, s"$column3needsEscaping", "x", prepareDataFrame())
      val rows = dataFrame.sparkDataFrame.collect()
      validateColumn(rows, Seq(0.540, 0.453, 0.362, 0.267, null))
      val schema = dataFrame.sparkDataFrame.schema
      schema.fieldNames shouldBe Array(column0, column1, column2, column3needsEscaping)
    }

    "fail when 2 comma-separated formulas are provided" in {
      intercept[SqlColumnExpressionSyntaxError] {
        val dataFrame = applyFormulaToDataFrame("MAX(x), SIN(x)", column1, "name", "x", prepareDataFrame())
        dataFrame.sparkDataFrame.collect()
      }; ()
    }

    "fail when formula is not correct" in {
      intercept[ActionExecutionError] {
        val dataFrame = applyFormulaToDataFrame("MAX(", "name", column1, "x", prepareDataFrame())
        dataFrame.sparkDataFrame.collect()
      }; ()
    }

    "produce NaN if the argument given to the function is not correct" in {
      // counting LN from negative number
      val dataFrame = applyFormulaToDataFrame("LN(x)", column1, column3, "x", prepareDataFrame())
      val rowWithNegativeValue = 1
      val rowWithNaN = dataFrame.sparkDataFrame.collect()(rowWithNegativeValue)
      rowWithNaN.getDouble(resultColumn).isNaN shouldBe true
    }

    "always create nullable columns" in {
      runTest("cast(1.0 as double)", column1, column3, Seq(1.0, 1.0, 1.0, 1.0, 1.0))
    }
  }

  def runTest(formula: String,
              inputColumnName: String,
              outputColumnName: String,
              expectedValues: Seq[Any],
              columnAlias: String = "x",
              resultType: NumericType = DoubleType) = {
    val dataFrame = applyFormulaToDataFrame(formula, inputColumnName, outputColumnName, columnAlias, prepareDataFrame())
    val rows = dataFrame.sparkDataFrame.collect()
    validateSchema(dataFrame.sparkDataFrame.schema, resultType)
    validateColumn(rows, expectedValues)
  }

  def applyFormulaToDataFrame(formula: String, inputColumnName: String, outputColumnName: String, columnAlias: String, df: DataFrame) = {
    val transformation = prepareTransformation(formula, inputColumnName, outputColumnName, columnAlias)
    applyTransformation(transformation, df)
  }

  def applyTransformation(transformation: SqlColumnTransformer, df: DataFrame) =
    transformation.applyTransformationAndSerialization(tempDir, df)

  def prepareTransformation(formula: String, inputColumnName: String, outputColumnName: String, columnAlias: String) = {
    val inPlace = NoInPlaceChoice().setOutputColumn(outputColumnName)
    val single = SingleColumnChoice().setInputColumn(NameSingleColumnSelection(inputColumnName)).setInPlaceChoice(inPlace)
    new SqlColumnTransformer().setFormula(formula).setSingleOrMultiChoice(single).setInputColumnAlias(columnAlias)
  }

  def validateSchema(schema: StructType, finalType: NumericType) = {
    schema.fieldNames shouldBe Array(column0, column1, column2, column3)
    schema.fields(0).dataType shouldBe StringType
    schema.fields(0) shouldBe 'nullable
    schema.fields(1).dataType shouldBe DoubleType
    schema.fields(1) shouldBe 'nullable
    schema.fields(2).dataType shouldBe DoubleType
    schema.fields(3) shouldBe 'nullable
    schema.fields(3).dataType shouldBe finalType
    schema.fields(3) shouldBe 'nullable
  }

  def validateColumn(rows: Array[Row], expectedValues: Seq[Any], column: Integer = resultColumn) = {
    forAll(expectedValues.zipWithIndex) { case (expectedVal, i) =>
      val value = rows(i).get(column)
      value match {
        case d: Double => d should equal(expectedVal.asInstanceOf[Double] +- delta)
        case _ => expectedVal shouldBe value
      }
    }
  }

  def prepareDataFrame() = {
    val manualRowsSeq = Seq(
      Row("aaa", 1.0, 0.2),
      Row("bbb", -1.1, 2.2),
      Row("ccc", 1.2, null),
      Row("ddd", -1.3, 4.2),
      Row("eee", null, null)
    )
    createDataFrame(manualRowsSeq, schema)
  }

  val schema = StructType(
    List(StructField(column0, StringType), StructField(column1, DoubleType), StructField(column2, DoubleType))
  )
}