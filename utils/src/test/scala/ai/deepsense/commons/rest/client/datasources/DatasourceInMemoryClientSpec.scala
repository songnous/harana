package com.harana.utils.rest.client.datasources

import java.util.UUID

import com.harana.api.datasourcemanager.model.Datasource
import com.harana.utils.serialization.Serialization
import com.harana.utils.StandardSpec
import com.harana.utils.UnitTestSupport

class DatasourceInMemoryClientSpec extends StandardSpec with UnitTestSupport with Serialization {

  val uuid = "123e4567-e89b-12d3-a456-426655440000"

  val notPresentUuid = "123e4567-e89b-12d3-a456-426655440001"

  val ds = getTestDatasource

  val testDatasourceList = List(ds)

  "DatasourceInMemoryClient" should {
    val datasourceClient = new DatasourceInMemoryClient(testDatasourceList)
    "return datasource if present" in {
      val dsOpt = datasourceClient.getDatasource(UUID.fromString(uuid))
      dsOpt shouldBe Some(ds)
    }
    "return None if datasource was not present in list" in {
      val dsOpt = datasourceClient.getDatasource(UUID.fromString(notPresentUuid))
      dsOpt shouldBe None
    }
  }

  private def getTestDatasource: Datasource = {
    val ds = new Datasource
    ds.setId(uuid)
    ds
  }

}
