package com.harana.sdk.backend.models.flow.actionobjects

import org.apache.spark.sql.Row
import org.apache.spark.sql.types.DoubleType
import org.apache.spark.sql.types.StringType
import org.apache.spark.sql.types.StructField
import org.apache.spark.sql.types.StructType
import com.harana.sdk.backend.models.flow.{IntegratedTestSupport, Knowledge}
import com.harana.sdk.backend.models.flow.actionobjects.dataframe.DataFrame
import com.harana.sdk.shared.models.flow.parameters.ParameterPair
import com.harana.spark.Linalg.Vectors

abstract class AbstractEvaluatorSmokeTest extends IntegratedTestSupport {

  def className: String

  val evaluator: Evaluator

  val evaluatorParameters: Seq[ParameterPair[_]]

  val inputDataFrameSchema = StructType(
    Seq(
      StructField("s", StringType),
      StructField("prediction", DoubleType),
      StructField("rawPrediction", new com.harana.spark.Linalg.VectorUDT),
      StructField("label", DoubleType)
    )
  )

  val inputDataFrame = {
    val rowSeq = Seq(
      Row("aAa bBb cCc dDd eEe f", 1.0, Vectors.dense(2.1, 2.2, 2.3), 3.0),
      Row("das99213 99721 8i!#@!", 4.0, Vectors.dense(5.1, 5.2, 5.3), 6.0)
    )
    createDataFrame(rowSeq, inputDataFrameSchema)
  }

  def setUpStubs() = ()

  className should {

    "successfully run _evaluate()" in {
      setUpStubs()
      evaluator.set(evaluatorParameters: _*)._evaluate(executionContext, inputDataFrame)
    }

    "successfully run _infer()" in {
      evaluator.set(evaluatorParameters: _*)._infer(Knowledge(inputDataFrame))
    }

    "successfully run report" in {
      evaluator.set(evaluatorParameters: _*).report()
    }
  }
}
