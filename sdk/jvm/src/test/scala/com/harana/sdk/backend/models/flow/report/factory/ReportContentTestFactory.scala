package com.harana.sdk.backend.models.flow.report.factory

import com.harana.sdk.shared.models.designer.flow.report
import com.harana.sdk.shared.models.flow.report
import com.harana.sdk.shared.models.flow.report.{ReportContent, ReportType}
import org.apache.spark.sql.types.DoubleType
import org.apache.spark.sql.types.IntegerType
import org.apache.spark.sql.types.StructField
import org.apache.spark.sql.types.StructType

trait ReportContentTestFactory {

  import ReportContentTestFactory._

  def testReport: ReportContent = ReportContent(
    reportName,
    reportType,
    Seq(TableTestFactory.testEmptyTable),
    Map(
      ReportContentTestFactory.categoricalDistName ->
        DistributionTestFactory.testCategoricalDistribution(ReportContentTestFactory.categoricalDistName),
      ReportContentTestFactory.continuousDistName  ->
        DistributionTestFactory.testContinuousDistribution(ReportContentTestFactory.continuousDistName)
    )
  )

}

object ReportContentTestFactory extends ReportContentTestFactory {

  val continuousDistName = "continuousDistributionName"

  val categoricalDistName = "categoricalDistributionName"

  val reportName = "TestReportContentName"

  val reportType = ReportType.Empty

  val someReport: ReportContent = report.ReportContent("empty", ReportType.Empty)

}
