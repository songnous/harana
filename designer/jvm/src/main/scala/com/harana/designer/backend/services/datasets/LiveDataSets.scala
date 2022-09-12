package com.harana.designer.backend.services.datasets

import com.harana.designer.backend.services.Crud
import com.harana.designer.backend.services.datasets.DataSets.Service
import com.harana.id.jwt.modules.jwt.JWT
import com.harana.modules.mongo.Mongo
import com.harana.modules.vertx.models.Response
import com.harana.modules.core.config.Config
import com.harana.modules.core.logger.Logger
import com.harana.modules.core.micrometer.Micrometer
import com.harana.sdk.shared.models.data.DataSet
import io.vertx.ext.web.RoutingContext
import zio.{Task, ZLayer}

object LiveDataSets {
  val layer = ZLayer.fromServices { (config: Config.Service,
                                     jwt: JWT.Service,
                                     logger: Logger.Service,
                                     micrometer: Micrometer.Service,
                                     mongo: Mongo.Service) => new Service {

    def list(rc: RoutingContext): Task[Response] = Crud.listResponse[DataSet]("Datasets", rc, config, jwt, logger, micrometer, mongo)
    def tags(rc: RoutingContext): Task[Response] = Crud.tagsResponse[DataSet]("Datasets", rc, config, jwt, logger, micrometer, mongo)
    def owners(rc: RoutingContext): Task[Response] = Crud.ownersResponse[DataSet]("Datasets", rc, config, jwt, logger, micrometer, mongo)
    def search(rc: RoutingContext): Task[Response] = Crud.searchResponse[DataSet]("Datasets", rc, config, jwt, logger, micrometer, mongo)
    def get(rc: RoutingContext): Task[Response] = Crud.getResponse[DataSet]("Datasets", rc, config, jwt, logger, micrometer, mongo)
    def delete(rc: RoutingContext): Task[Response] = Crud.deleteResponse[DataSet]("Datasets", rc, config, jwt, logger, micrometer, mongo)
    def create(rc: RoutingContext): Task[Response] = Crud.createResponse[DataSet]("Datasets", rc, config, jwt, logger, micrometer, mongo)
    def update(rc: RoutingContext): Task[Response] = Crud.updateResponse[DataSet]("Datasets", rc, config, jwt, logger, micrometer, mongo)

  }}
}