package com.harana.sdk.backend.models.flow.actiontypes

import com.harana.sdk.backend.models.flow._
import com.harana.sdk.backend.models.flow.ExecutionContext
import com.harana.sdk.backend.models.flow.actionobjects.dataframe.DataFrame
import com.harana.sdk.shared.models.flow.actiontypes.{SplitInfo, SplitModeChoice}
import com.harana.sdk.shared.models.flow.parameters.Parameters
import com.harana.spark.SQL
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.Row

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}
import izumi.reflect.Tag

class Split extends ActionTypeType1To2[DataFrame, DataFrame, DataFrame]
  with SplitInfo
  with Parameters {

  def execute(df: DataFrame)(context: ExecutionContext): (DataFrame, DataFrame) = {
    implicit val inputDataFrame = df
    implicit val exedcutionContext = context

    getSplitMode match {
      case randomChoice: SplitModeChoice.Random => executeRandomSplit(randomChoice)
      case conditionalChoice: SplitModeChoice.Conditional => executeConditionalSplit(conditionalChoice)
    }
  }

  private def executeRandomSplit(randomChoice: SplitModeChoice.Random)(implicit context: ExecutionContext, df: DataFrame) = {
    val Array(f1: RDD[Row], f2: RDD[Row]) = randomSplit(df, randomChoice.getSplitRatio, randomChoice.getSeed)
    val schema = df.sparkDataFrame.schema
    val dataFrame1 = context.dataFrameBuilder.buildDataFrame(schema, f1)
    val dataFrame2 = context.dataFrameBuilder.buildDataFrame(schema, f2)
    (dataFrame1, dataFrame2)
  }

  private def randomSplit(df: DataFrame, range: Double, seed: Long) =
    df.sparkDataFrame.rdd.randomSplit(Array(range, 1.0 - range), seed)

  private def executeConditionalSplit(conditionalChoice: SplitModeChoice.Conditional)(implicit context: ExecutionContext, df: DataFrame) = {
    import scala.concurrent.ExecutionContext.Implicits.global

    val condition = conditionalChoice.getCondition

    val inputDataFrameId = "split_conditional_" + java.util.UUID.randomUUID.toString.replace('-', '_')

    SQL.registerTempTable(df.sparkDataFrame, inputDataFrameId)
    println(s"Table '$inputDataFrameId' registered. Executing the expression")

    val selectFromExpression = s"SELECT * FROM $inputDataFrameId"

    val (leftExpression, rightExpression) = (s"$selectFromExpression WHERE $condition", s"$selectFromExpression WHERE not ($condition)")

    def runExpression(expression: String) = {
      val sqlResult = SQL.sparkSQLSession(df.sparkDataFrame).sql(expression)
      DataFrame.fromSparkDataFrame(sqlResult)
    }

    val results = Future.sequence(Seq(Future(runExpression(leftExpression)), Future(runExpression(rightExpression)))).map {
      case Seq(leftDataFrame, rightDataFrame) => (leftDataFrame, rightDataFrame)
    }

    results.onComplete { _ =>
      println(s"Unregistering the temporary table '$inputDataFrameId'")
      SQL.sparkSQLSession(df.sparkDataFrame).dropTempTable(inputDataFrameId)
    }

    Await.result(results, Duration.Inf)
  }

  lazy val tTagTO_0: Tag[DataFrame] = typeTag
  lazy val tTagTO_1: Tag[DataFrame] = typeTag

}