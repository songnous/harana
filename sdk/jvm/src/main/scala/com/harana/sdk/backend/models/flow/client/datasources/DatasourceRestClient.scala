package com.harana.sdk.backend.models.flow.client.datasources

import com.harana.sdk.backend.models.flow.utils.Logging

import java.net.URL
import java.util.UUID
class DatasourceRestClient(datasourceServerAddress: URL, userId: String) extends DatasourceClient with Logging {

//  private val client = {
//    val apiClient = new ApiClient()
//    apiClient.setAdapterBuilder(apiClient.getAdapterBuilder.baseUrl(datasourceServerAddress.toString))
//    apiClient.createService(classOf[DefaultApi])
//  }
//
//  def getDatasource(uuid: UUID): Option[Datasource] = {
//    val response = client.getDatasource(userId, uuid.toString).execute()
//    if (response.isSuccessful)
//      Some(response.body)
//    else
//      None
//  }

  override def getDatasource(uuid: UUID) = null
}

class DatasourceRestClientFactory(datasourceServerAddress: URL, userId: String) extends DatasourceClientFactory {

  def createClient: DatasourceRestClient = new DatasourceRestClient(datasourceServerAddress, userId)

}
