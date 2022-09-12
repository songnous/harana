package com.harana.modules.salesforce

import com.harana.modules.salesforce.models.{SalesforceError, SalesforceQuota}
import io.circe.Json
import zio.macros.accessible
import zio.{Has, IO}

@accessible
object Salesforce {
  type Salesforce = Has[Salesforce.Service]

  trait Service {
    def quota: IO[SalesforceError, SalesforceQuota]

    def describeObject(name: String): IO[SalesforceError, Json]

    def objectList: IO[SalesforceError, Json]

    def objectNames: IO[SalesforceError, List[String]]
  }
}