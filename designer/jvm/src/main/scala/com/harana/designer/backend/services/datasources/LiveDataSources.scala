package com.harana.designer.backend.services.datasources

import com.harana.designer.backend.services.Crud
import com.harana.designer.backend.services.Crud.userId
import com.harana.designer.backend.services.datasources.DataSources.Service
import com.harana.id.jwt.modules.jwt.JWT
import com.harana.modules.airbyte.{Airbyte, AirbyteSyncDirection}
import com.harana.modules.core.config.Config
import com.harana.modules.core.logger.Logger
import com.harana.modules.core.micrometer.Micrometer
import com.harana.modules.mongo.Mongo
import com.harana.modules.vertx.models.Response
import com.harana.sdk.shared.models.common.User.UserId
import com.harana.sdk.shared.models.common.Visibility
import com.harana.sdk.shared.models.data.{DataSource, DataSourceType, SyncDirection}
import com.harana.sdk.shared.models.flow.parameters.ParameterGroup
import io.circe.syntax._
import io.vertx.ext.web.RoutingContext
import org.mongodb.scala.bson.Document
import zio.{Task, ZLayer}

object LiveDataSources {
  val layer = ZLayer.fromServices { (airbyte: Airbyte.Service,
                                     config: Config.Service,
                                     jwt: JWT.Service,
                                     logger: Logger.Service,
                                     micrometer: Micrometer.Service,
                                     mongo: Mongo.Service) => new Service {

    def list(rc: RoutingContext): Task[Response] = Crud.listResponse[DataSource]("DataSources", rc, config, jwt, logger, micrometer, mongo)
    def tags(rc: RoutingContext): Task[Response] = Crud.tagsResponse[DataSource]("DataSources", rc, config, jwt, logger, micrometer, mongo)
    def owners(rc: RoutingContext): Task[Response] = Crud.ownersResponse[DataSource]("DataSources", rc, config, jwt, logger, micrometer, mongo)
    def search(rc: RoutingContext): Task[Response] = Crud.searchResponse[DataSource]("DataSources", rc, config, jwt, logger, micrometer, mongo)
    def get(rc: RoutingContext): Task[Response] = Crud.getResponse[DataSource]("DataSources", rc, config, jwt, logger, micrometer, mongo)
    def delete(rc: RoutingContext): Task[Response] = Crud.deleteResponse[DataSource]("DataSources", rc, config, jwt, logger, micrometer, mongo)
    def create(rc: RoutingContext): Task[Response] = Crud.createResponse[DataSource]("DataSources", rc, config, jwt, logger, micrometer, mongo)
    def update(rc: RoutingContext): Task[Response] = Crud.updateResponse[DataSource]("DataSources", rc, config, jwt, logger, micrometer, mongo)

    private val cachedDataSourceTypes =
      for {
        integrations      <- airbyte.integrations
        dataSourceTypes   =  integrations.map { ai =>
                                DataSourceType(
                                  id = s"${ai.syncDirection}-${ai.name}".toLowerCase,
                                  name = ai.name,
                                  supportsDBT = ai.supportsDBT,
                                  supportsIncremental = ai.supportsIncremental,
                                  supportsNormalization = ai.supportsNormalization,
                                  syncDirection = ai.syncDirection match {
                                    case AirbyteSyncDirection.Source => SyncDirection.Source
                                    case AirbyteSyncDirection.Destination => SyncDirection.Destination
                                  },
                                  parameterGroups = List(ParameterGroup(Some(s"${ai.syncDirection}-${ai.name}".toLowerCase), ai.properties.map(toParameter): _*))
                                )
                              }
        sorted             = dataSourceTypes.sortBy(_.name)
      } yield sorted


    def listWithTypeId(rc: RoutingContext): Task[Response] =
      for {
        typeId            <- Task(rc.pathParam("typeId"))
        userId            <- userId(rc, config, jwt)
        entities          <- mongo.findEquals[DataSource]("DataSources", Map("dataSourceType" -> typeId, "$or" -> creatorOrPublic(userId))).map(_.headOption)
        response          =  Response.JSON(entities.asJson)
      } yield response


    def typeWithId(rc: RoutingContext): Task[Response] =
      for {
        id                <- Task(rc.pathParam("id"))
        dataSourceTypes   <- cachedDataSourceTypes
        dataSourceType    =  dataSourceTypes.find(_.id == id)
        response          =  if (dataSourceType.isDefined) Response.JSON(dataSourceType.get.asJson) else Response.Empty(statusCode = Some(404))
      } yield response


    def typesWithDirection(rc: RoutingContext): Task[Response] =
      for {
        direction         <- Task(rc.pathParam("direction"))
        dataSourceTypes   <- cachedDataSourceTypes
        filtered          =  dataSourceTypes.filter(_.syncDirection == SyncDirection.withValue(direction)).map(_.id)
        response          =  Response.JSON(filtered.asJson)
      } yield response


    def sync(rc: RoutingContext): Task[Response] =
      null


    private def creatorOrPublic(userId: UserId) = {
      val isPublic = Document("visibility" -> Visibility.Public.toString)
      val isCreator = Document("createdBy" -> userId)
      List(isPublic, isCreator)
    }
  }}
}