package com.harana.sdk.backend.models.flow.actionobjects

import StringIndexerEstimatorIntegSpec._
import com.harana.sdk.shared.models.flow.actionobjects.multicolumn.SingleColumnParameters.SingleTransformInPlaceChoices.NoInPlaceChoice
import com.harana.sdk.shared.models.flow.actionobjects.multicolumn.SingleColumnParameters.SingleTransformInPlaceChoices.YesInPlaceChoice
import com.harana.sdk.backend.models.flow.actionobjects.spark.wrappers.transformers.TransformerSerialization._
import com.harana.sdk.backend.models.flow.{IntegratedTestSupport, Knowledge}
import com.harana.sdk.backend.models.flow.actionobjects.dataframe.DataFrame
import com.harana.sdk.backend.models.flow.actionobjects.spark.wrappers.estimators.StringIndexerEstimator
import com.harana.sdk.backend.models.flow.actionobjects.spark.wrappers.models.{MultiColumnStringIndexerModel, SingleColumnStringIndexerModelParameter}
import com.harana.sdk.backend.models.flow.actionobjects.spark.wrappers.transformers.TransformerSerialization
import com.harana.sdk.backend.models.flow.inference.InferContext
import com.harana.sdk.shared.models.designer.flow.actionobjects.spark.wrappers.models.{MultiColumnStringIndexerModel, SingleColumnStringIndexerModelParameter}

class StringIndexerEstimatorIntegSpec extends IntegratedTestSupport with TransformerSerialization {

  import IntegratedTestSupport._

  "StringIndexerEstimator" should {

    "convert single column" in {
      val si = new StringIndexerEstimator()
      si.setSingleColumn("a", "overriddenBelow")

      val t = si.fit(executionContext)(())(inputDataFrame).asInstanceOf[SingleColumnStringIndexerModelParameter]
      t.setInputColumn("c")
      t.setSingleInPlaceChoice(NoInPlaceChoice().setOutputColumn("out"))

      val transformed = executeTransformation(t, inputDataFrame)
      assertDataFramesEqual(transformed, outputDataFrame, checkNullability = false)
    }

    "convert single column in-place" in {
      val si = new StringIndexerEstimator()
      si.setSingleColumn("a", "overriddenBelow")

      val t = si.fit(executionContext)(())(inputDataFrame).asInstanceOf[SingleColumnStringIndexerModelParameter]
      t.setInputColumn("c")
      t.setSingleInPlaceChoice(YesInPlaceChoice())
      t.transform(executionContext)(())(inputDataFrame)

      val transformed = executeTransformation(t, inputDataFrame)
      assertDataFramesEqual(transformed, outputDataFrameInPlace, checkNullability = false)
    }

    "convert multiple columns" in {
      val si = new StringIndexerEstimator()
      val outputPrefix = "idx_"
      si.setMultipleColumn(Set("a", "b"), outputPrefix)

      val t = si.fit(executionContext)(())(inputDataFrame).asInstanceOf[MultiColumnStringIndexerModel]

      val transformed = executeTransformation(t, inputDataFrame)
      assertDataFramesEqual(transformed, multiOutputDataFrame, checkRowOrder = true, checkNullability = false)
    }

    "convert multiple columns in-place" in {
      val si = new StringIndexerEstimator()
      si.setMultipleColumnInPlace(Set("a", "b"))

      val t = si.fit(executionContext)(())(inputDataFrame).asInstanceOf[MultiColumnStringIndexerModel]
      t.validateParameters shouldBe empty

      val transformed = executeTransformation(t, inputDataFrame)
      assertDataFramesEqual(transformed, multiOutputDataFrameInPlace, checkRowOrder = true, checkNullability = false)
    }

    "infer knowledge in single-column mode" in {
      val si = new StringIndexerEstimator()
      si.setSingleColumn("a", "out")

      val inputKnowledge: Knowledge[DataFrame] = Knowledge(Set(inputDataFrame))
      val (transformerKnowledge, _) = si.fit.infer(mock[InferContext])(())(inputKnowledge)

      val t = transformerKnowledge.single.asInstanceOf[SingleColumnStringIndexerModelParameter]
      t.setInputColumn("c")
      t.setSingleInPlaceChoice(NoInPlaceChoice().setOutputColumn("out"))

      val (outputKnowledge, _) = t.transform.infer(mock[InferContext])(())(inputKnowledge)
      val inferredSchema = outputKnowledge.single.schema.get
      assertSchemaEqual(inferredSchema, outputDataFrame.schema.get, checkNullability = false)
    }
    "infer knowledge in multi-column mode" in {
      val si           = new StringIndexerEstimator()
      val outputPrefix = "idx_"
      si.setMultipleColumn(Set("a", "b"), outputPrefix)

      val inputKnowledge: Knowledge[DataFrame] = Knowledge(Set(inputDataFrame))
      val (transformerKnowledge, _) = si.fit.infer(mock[InferContext])(())(inputKnowledge)
      val inf = transformerKnowledge.single.asInstanceOf[MultiColumnStringIndexerModel]

      val (outputKnowledge, _) = inf.transform.infer(mock[InferContext])(())(inputKnowledge)
      val inferredSchema = outputKnowledge.single.schema.get
      assertSchemaEqual(inferredSchema, multiOutputDataFrame.schema.get, checkNullability = false)
    }
  }

  private def executeTransformation(transformer: Transformer, inputDataFrame: DataFrame) =
    transformer.applyTransformationAndSerialization(tempDir, inputDataFrame)

  val testRows = Seq(
    TestRow("a", "bb", "a"),
    TestRow("aa", "b", "a"),
    TestRow("a", "b", "a"),
    TestRow("aaa", "b", "aa"),
    TestRow("aa", "bbb", "aaa"),
    TestRow("aa", "b", "aaa")
  )

  val inputDataFrame = createDataFrame(testRows)

  val indexesA: Map[String, Double] = Map(("a", 1), ("aa", 0), ("aaa", 2))

  val outputDataFrame = {
    val indexedRs = testRows.map { case TestRow(a, b, c) => IndexedRow(a, b, c, indexesA(c))}
    createDataFrame(indexedRs)
  }

  val outputDataFrameInPlace = {
    val indexedRs = testRows.map { case TestRow(a, b, c) => IndexedRowInPlace(a, b, indexesA(c))}
    createDataFrame(indexedRs)
  }

  val multiOutputDataFrame = {
    val indexesB: Map[String, Double] = Map(
      ("b", 0),
      ("bb", 1),
      ("bbb", 2)
    )

    val indexedRs = testRows.map { case TestRow(a, b, c) =>
      MultiIndexedRow(a, b, c, indexesA(a), indexesB(b))
    }

    createDataFrame(indexedRs)
  }

  val multiOutputDataFrameInPlace = {
    val indexesB: Map[String, Double] = Map(
      ("b", 0),
      ("bb", 1),
      ("bbb", 2)
    )

    val indexedRs = testRows.map { case TestRow(a, b, c) =>
      MultiIndexedInPlaceRow(indexesA(a), indexesB(b), c)
    }

    createDataFrame(indexedRs)
  }
}

object StringIndexerEstimatorIntegSpec {
  case class TestRow(a: String, b: String, c: String)
  case class IndexedRow(a: String, b: String, c: String, out: Double)
  case class IndexedRowInPlace(a: String, b: String, c: Double)
  case class MultiIndexedRow(a: String, b: String, c: String, idx_a: Double, idx_b: Double)
  case class MultiIndexedInPlaceRow(a: Double, b: Double, c: String)
}
