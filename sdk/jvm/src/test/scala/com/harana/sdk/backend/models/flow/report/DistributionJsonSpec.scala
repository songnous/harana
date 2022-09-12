package com.harana.sdk.backend.models.flow.report

import org.scalatestplus.mockito.MockitoSugar
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import com.harana.sdk.backend.models.designer.flow.report.factory.DistributionTestFactory
import com.harana.sdk.backend.models.flow.report.factory.DistributionTestFactory
import com.harana.sdk.shared.models.designer.flow.report.ReportJsonProtocol
import com.harana.sdk.shared.models.flow.report.{ContinuousDistribution, Distribution, NoDistribution, Statistics}
import io.circe.syntax.EncoderOps

class DistributionJsonSpec
    extends AnyWordSpec
    with MockitoSugar
    with DistributionTestFactory
    with Matchers
    {

  "NoDistribution" should {
    val noDistribution: Distribution = NoDistribution(
      DistributionTestFactory.distributionName,
      DistributionTestFactory.distributionDescription
    )
    val jsonNoDistribution = Json(
      "name"          -> DistributionTestFactory.distributionName,
      "subtype"       -> "no_distribution",
      "description"   -> DistributionTestFactory.distributionDescription,
      "missingValues" -> 0
    )
    "serialize to Json" in {
      val json = noDistribution.asJson
      json shouldBe jsonNoDistribution
    }
    "deserialize from Json" in {
      val distributionObject = jsonNoDistribution[Distribution]
      distributionObject shouldBe noDistribution
    }
  }

  "DiscreteDistribution" should {
    val jsonCategoricalDistribution = Json(
      "name"          -> DistributionTestFactory.distributionName,
      "subtype"       -> "discrete",
      "description"   -> DistributionTestFactory.distributionDescription,
      "missingValues" -> 0,
      "buckets"       ->
        Seq(DistributionTestFactory.categoricalDistributionBuckets.map(_)).toVector,
      "counts"        -> Seq(DistributionTestFactory.distributionCounts.map(_)).toVector
    )
    "serialize to Json" in {
      val json = testCategoricalDistribution.asJson
      json shouldBe jsonCategoricalDistribution
    }
    "deserialize from Json" in {
      jsonCategoricalDistribution.as[Distribution] shouldBe testCategoricalDistribution
    }
  }

  "ContinuousDistribution" should {
    val statistics                           = testStatistics
    val jsonContinuousDistribution = Json(
      "name"          -> DistributionTestFactory.distributionName,
      "subtype"       -> "continuous",
      "description"   -> DistributionTestFactory.distributionDescription,
      "missingValues" -> 0,
      "buckets"       ->
        Seq(DistributionTestFactory.continuousDistributionBuckets.map(_)).toVector,
      "counts"        -> Seq(DistributionTestFactory.distributionCounts.map(_)).toVector,
      "statistics"    -> expectedStatisticsJson(statistics)
    )
    "serialize to Json" in {
      val json = testContinuousDistribution.asJson
      json shouldBe jsonContinuousDistribution
    }
    "deserialize from Json" in {
      jsonContinuousDistribution.as[Distribution] shouldBe testContinuousDistribution
    }
    "throw IllegalArgumentException" when {
      def createContinousDistributionWith(buckets: Seq[String], counts: Seq[Long]): ContinuousDistribution =
        ContinuousDistribution("", "", 1, buckets, counts, testStatistics)
      "created with empty buckets and single count" in {
        an[IllegalArgumentException] shouldBe thrownBy(createContinousDistributionWith(Seq(), Seq(1)))
      }
      "created with buckets of size one" in {
        an[IllegalArgumentException] shouldBe thrownBy(createContinousDistributionWith(Seq("1"), Seq()))
      }
      "created with non empty buckets and counts of size != (buckets' size -1)" in {
        an[IllegalArgumentException] shouldBe thrownBy(
          createContinousDistributionWith(Seq("0.1", "0.2", "0.3"), Seq(1))
        )
      }
    }
  }

  "Statistics" should {
    val statisticsWithEmptyValues = testStatisticsWithEmptyValues
    "serialize to Json" in {
      val json = statisticsWithEmptyValues.asJson
      json shouldBe expectedStatisticsJson(statisticsWithEmptyValues)
    }
    "deserialize from Json" in {
      expectedStatisticsJson(statisticsWithEmptyValues).as[Statistics] shouldBe
        statisticsWithEmptyValues
    }
  }

  private def expectedStatisticsJson(statistics: Statistics) =
    Json(
      "max"  -> jsStringOrNull(statistics.max),
      "min"  -> jsStringOrNull(statistics.min),
      "mean" -> jsStringOrNull(statistics.mean)
    )

  private def jsStringOrNull(s: Option[String]) = s.map(Json.(_)).getOrElse(Json.Null)

}
