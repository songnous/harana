package com.harana.sdk.backend.models.flow.actions

import com.harana.sdk.backend.models.flow._
import com.harana.sdk.backend.models.flow.{ActionType, ExecutionContext, IntegratedTestSupport}
import com.harana.sdk.backend.models.flow.actionobjects.dataframe.DataFrame
import com.harana.sdk.shared.models.flow.ActionObjectInfo
import com.harana.sdk.shared.models.flow.actions.SplitModeChoice
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.Row
import org.apache.spark.sql.types.{IntegerType, StructField, StructType}
import org.scalatest.matchers.should.Matchers
import org.scalatestplus.scalacheck.ScalaCheckDrivenPropertyChecks

import scala.jdk.CollectionConverters._

class DataFrameSplitterIntegSpec extends IntegratedTestSupport with ScalaCheckDrivenPropertyChecks with Matchers {

  "SplitDataFrame" should {

    "split randomly one df into two df in given range" in {
      val input = Range(1, 100)
      val parameterPairs = List((0.0, 0), (0.3, 1), (0.5, 2), (0.8, 3), (1.0, 4))

      for ((splitRatio, seed) <- parameterPairs) {
        val rdd = createData(input)
        val df = executionContext.dataFrameBuilder.buildDataFrame(createSchema, rdd)
        val (df1, df2) = executeAction(
          executionContext,
          new Split().setSplitMode(SplitModeChoice.Random().setSplitRatio(splitRatio).setSeed(seed / 2))
        )(df)
        validateSplitProperties(df, df1, df2)
      }
    }

    "split conditionally one df into two df in given range" in {
      val input = Range(1, 100)
      val condition = "value > 20"
      val predicate: Int => Boolean = _ > 20

      val (expectedDF1, expectedDF2) = (input.filter(predicate), input.filter(!predicate(_)))

      val rdd = createData(input)
      val df = executionContext.dataFrameBuilder.buildDataFrame(createSchema, rdd)
      val (df1, df2) = executeAction(
        executionContext,
        new Split().setSplitMode(SplitModeChoice.Conditional().setCondition(condition))
      )(df)
      df1.sparkDataFrame.collect().map(_.get(0)) should contain theSameElementsAs expectedDF1
      df2.sparkDataFrame.collect().map(_.get(0)) should contain theSameElementsAs expectedDF2
      validateSplitProperties(df, df1, df2)
    }
  }

  private def createSchema = {
    StructType(
      List(
        StructField("value", IntegerType, nullable = false)
      )
    )
  }

  private def createData(data: Seq[Int]) = sparkContext.parallelize(data.map(Row(_)))

  private def executeAction(context: ExecutionContext, action: ActionType)(dataFrame: DataFrame) ={
    val actionResult = action.executeUntyped(List[ActionObjectInfo](dataFrame))(context)
    val df1 = actionResult.head.asInstanceOf[DataFrame]
    val df2 = actionResult.last.asInstanceOf[DataFrame]
    (df1, df2)
  }

  def validateSplitProperties(inputDF: DataFrame, outputDF1: DataFrame, outputDF2: DataFrame) = {
    val dfCount = inputDF.sparkDataFrame.count()
    val df1Count = outputDF1.sparkDataFrame.count()
    val df2Count = outputDF2.sparkDataFrame.count()
    val rowsDf = inputDF.sparkDataFrame.collectAsList().asScala
    val rowsDf1 = outputDF1.sparkDataFrame.collectAsList().asScala
    val rowsDf2 = outputDF2.sparkDataFrame.collectAsList().asScala
    val intersect = rowsDf1.intersect(rowsDf2)
    intersect.size shouldBe 0
    (df1Count + df2Count) shouldBe dfCount
    rowsDf.toSet shouldBe rowsDf1.toSet.union(rowsDf2.toSet)
  }
}
