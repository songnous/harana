package com.harana.sdk.backend.models.flow.actionobjects

import org.apache.spark.sql.types.DoubleType
import org.apache.spark.sql.types.StructField
import org.apache.spark.sql.types.StructType
import MultiColumnTransformerIntegSpec._
import com.harana.sdk.shared.models.flow.actionobjects.multicolumn.MultiColumnParameters.MultiColumnInPlaceChoices.MultiColumnNoInPlace
import com.harana.sdk.shared.models.flow.actionobjects.multicolumn.MultiColumnParameters.MultiColumnInPlaceChoices.MultiColumnYesInPlace
import com.harana.sdk.shared.models.flow.actionobjects.multicolumn.MultiColumnParameters.SingleOrMultiColumnChoices.MultiColumnChoice
import com.harana.sdk.shared.models.flow.actionobjects.multicolumn.MultiColumnParameters.SingleOrMultiColumnChoices.SingleColumnChoice
import com.harana.sdk.shared.models.flow.actionobjects.multicolumn.SingleColumnParameters.SingleTransformInPlaceChoices.NoInPlaceChoice
import com.harana.sdk.shared.models.flow.actionobjects.multicolumn.SingleColumnParameters.SingleTransformInPlaceChoices.YesInPlaceChoice
import com.harana.sdk.backend.models.flow.parameters
import com.harana.sdk.backend.models.flow.{ExecutionContext, IntegratedTestSupport, Knowledge}
import com.harana.sdk.backend.models.flow.actionobjects.dataframe.DataFrame
import com.harana.sdk.backend.models.flow.inference.InferContext
import com.harana.sdk.shared.models.flow.parameters.selections.{MultipleColumnSelection, NameColumnSelection, NameSingleColumnSelection}
import com.harana.sdk.shared.models.flow.parameters.{NumericParameter, Parameter, ParameterGroup}

class MultiColumnTransformerIntegSpec extends IntegratedTestSupport {

  val magicConstant: Double = 1337d

  import IntegratedTestSupport._

  "MultiColumnTransformer" should {

    "return also action specific parameters in json" in {
      val t = transformerWithMagicConstant
      t.parameters should contain(t.getMagicConstant)
    }
  }

  "MultiColumnTransformer" when {

    "working with multiple columns" when {
      val t = transformerWithMagicConstant
      t.setMultipleColumns(columns = Seq("x", "y"), inPlace = Some("magic_"))

      "in-place mode was not selected" should {

        "create columns with unique name (with prefix)" in {
          val transformedInPlace = t.transform(executionContext)(())(inputData)
          transformedInPlace.schema shouldBe expectedTransformedMulti.schema
          assertDataFramesEqual(transformedInPlace, expectedTransformedMulti)
        }

        "infer schema for columns with unique name (with prefix)" in {
          val (k, _) = t.transform.infer(mock[InferContext])(())(Knowledge(DataFrame.forInference(inputSchema.get)))
          assertSchemaEqual(k.single.schema.get, expectedTransformedMulti.schema.get, checkNullability = false)
        }
      }

      "in-place mode was selected" should {
        val t = transformerWithMagicConstant
        t.setMultipleColumns(columns = Seq("x", "y"), inPlace = None)

        "replace columns" in {
          val transformedInPlace = t.transform(executionContext)(())(inputData)
          assertDataFramesEqual(transformedInPlace, expectedMultiInPlace)
        }

        "replace columns schema" in {
          val (k, _) = t.transform.infer(mock[InferContext])(())(Knowledge(DataFrame.forInference(inputSchema.get)))
          assertSchemaEqual(k.single.schema.get, expectedMultiInPlace.schema.get, checkNullability = false)
        }
      }
    }

    "working with a single column" should {
      val t = transformerWithMagicConstant
      t.setSingleColumn(column = "y", inPlace = Some("updatedy"))

      "in-place mode was not selected" when {

        "create a new column" in {
          val transformedY = t.transform(executionContext)(())(inputData)
          transformedY.schema shouldBe expectedTransformedY.schema
          assertDataFramesEqual(transformedY, expectedTransformedY)
        }

        "infer schema for columns with unique name (with prefix)" in {
          val (k, _) = t.transform.infer(mock[InferContext])(())(Knowledge(DataFrame.forInference(inputSchema.get)))
          assertSchemaEqual(k.single.schema.get, expectedTransformedY.schema.get, checkNullability = false)
        }
      }

      "in-place mode was selected" when {
        val t = transformerWithMagicConstant
        t.setSingleColumn(column = "y", inPlace = None)

        "replace a column" in {
          val transformedY = t.transform(executionContext)(())(inputData)
          transformedY.schema shouldBe expectedTransformedYInPlace.schema
          assertDataFramesEqual(transformedY, expectedTransformedYInPlace)
        }

        "replace columns schema" in {
          val (k, _) = t.transform.infer(mock[InferContext])(())(Knowledge(DataFrame.forInference(inputSchema.get)))
          assertSchemaEqual(k.single.schema.get, expectedTransformedYInPlace.schema.get, checkNullability = false)
        }
      }
    }
  }

  def transformerWithMagicConstant: AddAConstantTransformer = {
    val t = AddAConstantTransformer()
    t.setMagicConstant(magicConstant)
    t
  }

  val rawInputData = Seq(
    InputData(3, "abc", 5, 23),
    InputData(14, "def", 5, 4),
    InputData(15, "ghi", 5, 89),
    InputData(29, "jkl", 5, 13)
  )

  val inputData = createDataFrame(rawInputData)
  val inputSchema = inputData.schema

  val expectedMultiInPlace = createDataFrame(
    rawInputData.map(d => InputDataDouble(d.x + magicConstant, d.a, d.y + magicConstant, d.z))
  )

  val expectedTransformedYInPlace = createDataFrame(
    rawInputData.map(d => InputDataDouble(d.x, d.a, d.y + magicConstant, d.z))
  )

  val expectedTransformedY = createDataFrame(
    rawInputData.map(d => InputDataUpdatedY(d.x, d.a, d.y, d.z, d.y + magicConstant))
  )

  val expectedTransformedMulti = createDataFrame(rawInputData.map { d =>
    InputDataUpdatedMulti(d.x, d.a, d.y, d.z, d.x + magicConstant, d.y + magicConstant)
  })

}

object MultiColumnTransformerIntegSpec {

  case class InputData(x: Double, a: String, y: Int, z: Double)
  case class InputDataDouble(x: Double, a: String, y: Double, z: Double)
  case class InputDataUpdatedY(x: Double, a: String, y: Int, z: Double, updatedy: Double)
  case class InputDataUpdatedMulti(x: Double, a: String, y: Int, z: Double, magic_x: Double, magic_y: Double)

  case class AddAConstantTransformer() extends MultiColumnTransformer {

    val magicConstantParameter = NumericParameter("aconstant")
    def getMagicConstant = $(magicConstantParameter)
    def setMagicConstant(value: Double): this.type = set(magicConstantParameter, value)

    val parameterGroups = List(ParameterGroup(None, magicConstantParameter))

    def transformSingleColumn(
        inputColumn: String,
        outputColumn: String,
        context: ExecutionContext,
        dataFrame: DataFrame
    ) = {
      transformSingleColumnSchema(inputColumn, outputColumn, dataFrame.sparkDataFrame.schema)
      DataFrame.fromSparkDataFrame(dataFrame.sparkDataFrame.selectExpr("*", s"cast(`$inputColumn` as double) + $getMagicConstant as `$outputColumn`"))
    }

    def transformSingleColumnSchema(inputColumn: String, outputColumn: String, schema: StructType): Option[StructType] = {
      if (schema.fieldNames.contains(outputColumn)) throw new IllegalArgumentException(s"Output column $outputColumn already exists.")
      val outputFields = schema.fields :+ StructField(outputColumn, DoubleType, nullable = false)
      Some(StructType(outputFields))
    }

    def setSingleColumn(column: String, inPlace: Option[String]) = {
      val inplaceChoice = inPlace match {
        case Some(x) => NoInPlaceChoice().setOutputColumn(x)
        case None => YesInPlaceChoice()
      }

      val single = SingleColumnChoice().setInputColumn(NameSingleColumnSelection(column)).setInPlaceChoice(inplaceChoice)
      setSingleOrMultiChoice(single)
    }

    def setMultipleColumns(columns: Seq[String], inPlace: Option[String]) = {
      val inPlaceChoice = inPlace match {
        case Some(x) => MultiColumnNoInPlace().setColumnsPrefix(x)
        case None => MultiColumnYesInPlace()
      }

      val columnSelection = NameColumnSelection(columns.toSet)
      val multiple = MultiColumnChoice().setInputColumns(MultipleColumnSelection(List(columnSelection))).setInPlaceChoice(inPlaceChoice)
      setSingleOrMultiChoice(multiple)
    }

  }
}