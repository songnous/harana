package com.harana.sdk.backend.models.flow.models.workflows

import com.harana.sdk.backend.models.flow.actionobjects.dataframe.DataFrame
import com.harana.sdk.shared.models.flow
import com.harana.sdk.shared.models.flow.EntitiesMap
import com.harana.sdk.shared.models.flow.report.ReportContent
import com.harana.sdk.shared.models.flow.utils.Entity
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import org.scalatestplus.mockito.MockitoSugar

class EntitiesMapSpec extends AnyWordSpec with Matchers with MockitoSugar {

  "EntitiesMap" should {
    "be correctly created from results and reports" in {

      val entity1Id = Entity.Id.randomId
      val actionObject1 = DataFrame()
      val report1 = mock[ReportContent]

      val entity2Id = Entity.Id.randomId
      val actionObject2 = DataFrame()

      val results = Map(entity1Id -> actionObject1, entity2Id -> actionObject2)
      val reports = Map(entity1Id -> report1)

      flow.EntitiesMap(results, reports) shouldBe EntitiesMap(
        Map(
          entity1Id -> EntitiesMap.Entry("com.harana.sdk.backend.models.flow.actionobjects.dataframe.DataFrame", Some(report1)),
          entity2Id -> EntitiesMap.Entry("com.harana.sdk.backend.models.flow.actionobjects.dataframe.DataFrame", None)
        )
      )
    }
  }
}
