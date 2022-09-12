package com.harana.sdk.backend.models.flow.client.datasources

import java.util.UUID

// FIXME: Harana needs to replace this
case class Datasource()

trait DatasourceClient {
  def getDatasource(uuid: UUID): Option[Datasource]
}

trait DatasourceClientFactory {
  def createClient: DatasourceClient
}

object DatasourceTypes {
  type DatasourceId = String
  type DatasourceMap = Map[DatasourceId, Datasource]
  type DatasourceList = List[Datasource]
}
