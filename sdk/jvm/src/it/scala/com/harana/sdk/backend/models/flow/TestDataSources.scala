package com.harana.sdk.backend.models.flow

import com.harana.sdk.backend.models.flow.client.datasources.DatasourceInMemoryClient

trait TestDataSources {
  self: TestFiles =>

  lazy val datasourceClient = new DatasourceInMemoryClient(
    someDatasourcesForReading ++ someDatasourcesForWriting
  )

  lazy val someDatasourcesForReading = List(someExternalJsonDatasource, someExternalCsvDatasource)
  lazy val someDatasourcesForWriting = List(someLibraryCsvDatasource, someLibraryJsonDatasource)

  // FIXME
  private lazy val someLibraryJsonDatasource = {
    null
  }

  private lazy val someLibraryCsvDatasource = {
    null
  }

  private lazy val someExternalJsonDatasource = {
      null
  }

  private lazy val someExternalCsvDatasource = {
       null
  }
}
