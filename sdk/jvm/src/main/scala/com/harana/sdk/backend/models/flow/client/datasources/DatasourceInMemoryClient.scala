package com.harana.sdk.backend.models.flow.client.datasources

import java.util.UUID

class DatasourceInMemoryClient(datasourceList: List[Datasource]) extends DatasourceClient {

  def getDatasource(uuid: UUID): Option[Datasource] = null

  // FIXME: Harana needs to replace this
  //def getDatasource(uuid: UUID): Option[Datasource] = datasourceList.find(uuid.toString)
  //private val datasourceMap = datasourceList.lookupBy(_.getId)
}

class DatasourceInMemoryClientFactory(datasourceMap: List[Datasource]) extends DatasourceClientFactory {
  def createClient: DatasourceClient = new DatasourceInMemoryClient(datasourceMap)
}
