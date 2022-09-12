package com.harana.utils.json.envelope

import org.joda.time.DateTime
import org.scalatestplus.mockito.MockitoSugar

import com.harana.api.datasourcemanager.model.AccessLevel
import com.harana.api.datasourcemanager.model.Datasource
import com.harana.api.datasourcemanager.model.DatasourceParams
import com.harana.api.datasourcemanager.model.DatasourceType
import com.harana.utils.datasource.DatasourceTestData
import com.harana.utils.json.datasources.DatasourceListJsonProtocol
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class DatasourceListJsonProtocolSpec extends AnyWordSpec with MockitoSugar with Matchers {

  val uuid = "123e4567-e89b-12d3-a456-426655440000"

  val externalFile = DatasourceType.EXTERNALFILE

  val dsList = List(DatasourceTestData.multicharSeparatorLibraryCsvDatasource)

  "DatasourceJsonProtocolSpec" should {
    "serialize and deserialize single datasource" in {
      val datasourcesJson = DatasourceListJsonProtocol.toString(dsList)
      val asString        = datasourcesJson.toString
      val datasources     = DatasourceListJsonProtocol.fromString(asString)
      info(s"Datasource: $datasources, json: $asString")
      datasources should contain theSameElementsAs dsList
    }

    "serialize no datasource" in {
      val datasourcesJson = DatasourceListJsonProtocol.toString(List.empty[Datasource])
      datasourcesJson shouldBe "[]"
    }
  }

}
