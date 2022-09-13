package com.harana.modules.airbyte

import io.airbyte.protocol.models.{AirbyteCatalog, AirbyteConnectionStatus, ConfiguredAirbyteCatalog}
import zio.macros.accessible
import zio.{Has, Task, UIO}

@accessible
object Airbyte {
  type Airbyte = Has[Airbyte.Service]

  trait Service {

    def integrations: Task[List[AirbyteIntegration]]

    def discover(integrationName: String, connectionValues: Map[String, Object]): Task[AirbyteCatalog]

    def check(integrationName: String, connectionValues: Map[String, Object]): Task[AirbyteConnectionStatus]

//    def schema(integrationName: String): Task[AirbyteSchema]

    def read(integrationName: String, catalog: ConfiguredAirbyteCatalog)

  }
}