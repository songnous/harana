package com.harana.sdk.backend.models.flow.actionobjects

import org.apache.spark.sql.Row
import org.apache.spark.sql.types.DoubleType
import org.apache.spark.sql.types.StructField
import org.apache.spark.sql.types.StructType
import org.mockito.Mockito._

class PythonEvaluatorSmokeTest extends AbstractEvaluatorSmokeTest {

  def className = "PythonEvaluator"

  val evaluator = new PythonEvaluator()
  val evaluatorParameters = Seq()

  override def setUpStubs() = {
    val someMetric = Seq[Row](Row(1.0))
    val metricDF = createDataFrame(someMetric, StructType(Seq(StructField("metric", DoubleType, nullable = false))))
    when(executionContext.dataFrameStorage.getOutputDataFrame(0)).thenReturn(Some(metricDF.sparkDataFrame))
  }
}
