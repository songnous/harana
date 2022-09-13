package com.harana.sdk.backend.models.flow.report

import com.harana.sdk.backend.models.flow.report.factory.ReportContentTestFactory
import com.harana.sdk.backend.models.flow.report.factory.ReportContentTestFactory
import com.harana.sdk.shared.models.designer.flow.report.ReportJsonProtocol
import com.harana.sdk.shared.models.flow.report.ReportContent
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec


class ReportContentJsonSpec extends AnyWordSpec with Matchers with ReportContentTestFactory {

  import ReportContentTestFactory._

  "ReportContent" should {

    val emptyReportJson = Json(
      "name"          -> reportName,
      "reportType"    -> reportType.toString,
      "tables"        -> Seq(),
      "distributions" -> Json.Null
    )
    val report                    = testReport
    val reportJson: Json      = Json(
      "name"          -> reportName,
      "reportType"    -> reportType.toString,
      "tables"        -> Seq(report.tables.map(_.asJson): _*),
      "distributions" -> Json(report.distributions.mapValues(_.asJson))
    )

    "serialize" when {
      "empty" in {
        val report = ReportContent(reportName, reportType)
        report.asJson shouldBe emptyReportJson
      }
      "filled report" in {
        val json = report.asJson
        json shouldBe reportJson
      }
    }
    "deserialize" when {
      "empty report" in {
        emptyReportJson.as[ReportContent] shouldBe ReportContent(reportName, reportType)
      }
      "full report" in {
        reportJson.as[ReportContent] shouldBe report
      }
    }
  }
}
