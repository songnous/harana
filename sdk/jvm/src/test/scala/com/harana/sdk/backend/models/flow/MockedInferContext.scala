package com.harana.sdk.backend.models.flow

import com.harana.sdk.backend.models.flow.Catalog.{ActionCatalog, ActionObjectCatalog}
import com.harana.sdk.backend.models.flow.actionobjects.dataframe.DataFrameBuilder
import com.harana.sdk.backend.models.flow.client.datasources.{DatasourceClient, DatasourceInMemoryClientFactory}
import com.harana.sdk.backend.models.flow.Catalog.{ActionCatalog, ActionObjectCatalog}
import com.harana.sdk.backend.models.flow.client.datasources.DatasourceClient
import org.scalatestplus.mockito.MockitoSugar.mock

object MockedInferContext {

  def apply(actionObjectCatalog: ActionObjectCatalog = mock[ActionObjectCatalog],
            dataFrameBuilder: DataFrameBuilder = mock[DataFrameBuilder],
            actionCatalog: ActionCatalog = mock[ActionCatalog],
            datasourceClient: DatasourceClient = new DatasourceInMemoryClientFactory(List.empty).createClient) = {
    inference.InferContext(dataFrameBuilder, actionCatalog, actionObjectCatalog)
  }
}
