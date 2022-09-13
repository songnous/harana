package com.harana.sdk.backend.models.flow.actionobjects.dataframe.report.distribution

import java.sql.Timestamp
import org.apache.spark.rdd.RDD
import org.apache.spark.sql
import org.apache.spark.sql.Row
import org.apache.spark.sql.types._
import com.harana.sdk.backend.models.flow.report._
import com.harana.sdk.backend.models.flow.IntegratedTestSupport
import com.harana.sdk.backend.models.flow.actionobjects.dataframe.{DataFrame, DataFrameTestFactory}
import com.harana.sdk.shared.models.designer.flow.utils.DateTimeConverter
import com.harana.sdk.shared.models.flow.report.ContinuousDistribution

class StatisticsForContinuousIntegSpec extends IntegratedTestSupport with DataFrameTestFactory {

  "Statistics (Min, max and mean values)" should {
    "be calculated for each continuous column in distribution" when {
      "data is of type int" in {
        val distribution = distributionForInt(1, 2, 3, 4, 5)
        distribution.statistics.min shouldEqual Some("1")
        distribution.statistics.max shouldEqual Some("5")
        distribution.statistics.mean shouldEqual Some("3")
      }
      "data is of type Timestamp" in {
        val distribution =
          distributionForTimestamps(new Timestamp(1000), new Timestamp(2000), new Timestamp(3000))
        distribution.statistics.min shouldEqual Some(formatDate(1000))
        distribution.statistics.max shouldEqual Some(formatDate(3000))
        distribution.statistics.mean shouldEqual Some(formatDate(2000))
      }
    }
  }
  "Null value in data" should {
    val distribution = distributionForDouble(1, 2, 3, 4, Double.NaN, 5)
    "not be skipped in calculating min and max" in {
      distribution.statistics.min shouldEqual Some("1")
      distribution.statistics.max shouldEqual Some("5")
    }
    "result in mean value NaN" in {
      distribution.statistics.mean shouldEqual Some("NaN")
    }
  }

  lazy val columnName = "column_name"

  private def distributionForDouble(data: Double*): ContinuousDistribution =
    distributionFor(data, DoubleType)

  private def distributionForInt(data: Int*): ContinuousDistribution =
    distributionFor(data, IntegerType)

  private def distributionForTimestamps(data: Timestamp*): ContinuousDistribution =
    distributionFor(data, TimestampType)

  private def distributionFor(data: Seq[Any], dataType: DataType): ContinuousDistribution = {
    val schema = StructType(
      Array(
        StructField(columnName, dataType)
      )
    )

    val rows      = data.map(v => Row(v))
    val dataFrame = createDataFrame(rows, schema)

    val report = dataFrame.report()
    report.content.distributions(columnName).asInstanceOf[ContinuousDistribution]
  }

  def buildDataFrame(schema: StructType, data: RDD[Row]) = {
    val dataFrame: sql.DataFrame = sparkSQLSession.createDataFrame(data, schema)
    DataFrame.fromSparkDataFrame(dataFrame)
  }

  def formatDate(millis: Long) =
    DateTimeConverter.toString(DateTimeConverter.fromMillis(millis))

}
