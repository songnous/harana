package com.harana.modules.airbyte

import com.harana.modules.airbyte.Airbyte.Service
import com.harana.modules.core.config.Config
import com.harana.modules.core.logger.Logger
import com.harana.modules.core.micrometer.Micrometer
import com.harana.modules.kubernetes.Kubernetes
import io.airbyte.protocol.models.{AirbyteCatalog, AirbyteConnectionStatus, ConfiguredAirbyteCatalog}
import io.circe.parser._

import java.nio.file.{Files, Paths}
import scala.collection.JavaConverters._
import zio.{Task, UIO, ZLayer}

object LiveAirbyte {
  val layer = ZLayer.fromServices { (config: Config.Service,
                                     kubernetes: Kubernetes.Service,
                                     logger: Logger.Service,
                                     micrometer: Micrometer.Service) => new Service {

    def integrations: Task[List[AirbyteIntegration]] =
      for {
        files         <- Task(airbyteFiles)
        jsons         = files.mapValues(parse).filter(_._2.isRight).mapValues(_.right.get)
        integrations  = jsons.map { j => toAirbyteIntegration(j._1, j._2)}.toList
      } yield integrations


    def discover(integrationName: String, connectionValues: Map[String, Object]): Task[AirbyteCatalog] =
      Task(null)


    def check(integrationName: String, connectionValues: Map[String, Object]): Task[AirbyteConnectionStatus] =
      Task(null)

//    def schema(integrationName: String): Task[Any] =
//      Task(null)

    def read(integrationName: String, catalog: ConfiguredAirbyteCatalog): Unit =
      Task(null)
  }}
}