package com.harana.designer.backend.services.schedules

import com.harana.designer.backend.services.Crud
import com.harana.designer.backend.services.schedules.Schedules.Service
import com.harana.designer.backend.services.schedules.argo.ArgoScheduler
import com.harana.id.jwt.modules.jwt.JWT
import com.harana.modules.core.config.Config
import com.harana.modules.core.logger.Logger
import com.harana.modules.core.micrometer.Micrometer
import com.harana.modules.mongo.Mongo
import com.harana.modules.vertx.models.Response
import com.harana.sdk.shared.models.schedules.{Action, Event, Schedule}
import io.circe.syntax._
import io.vertx.ext.web.RoutingContext
import zio.{Task, ZLayer}

object LiveSchedules {
  val layer = ZLayer.fromServices { (config: Config.Service,
                                     jwt: JWT.Service,
                                     logger: Logger.Service,
                                     micrometer: Micrometer.Service,
                                     mongo: Mongo.Service,
                                     scheduler: ArgoScheduler.Service) => new Service {

      def actionTypes(rc: RoutingContext): Task[Response] = Task(Response.JSON(Action.types.asJson))
      def eventTypes(rc: RoutingContext): Task[Response] = Task(Response.JSON(Event.types.asJson))

      def list(rc: RoutingContext): Task[Response] = Crud.listResponse[Schedule]("Schedules", rc, config, jwt, logger, micrometer, mongo)
      def tags(rc: RoutingContext): Task[Response] = Crud.tagsResponse[Schedule]("Schedules", rc, config, jwt, logger, micrometer, mongo)
      def owners(rc: RoutingContext): Task[Response] = Crud.ownersResponse[Schedule]("Schedules", rc, config, jwt, logger, micrometer, mongo)
      def search(rc: RoutingContext): Task[Response] = Crud.searchResponse[Schedule]("Schedules", rc, config, jwt, logger, micrometer, mongo)
      def get(rc: RoutingContext): Task[Response] = Crud.getResponse[Schedule]("Schedules", rc, config, jwt, logger, micrometer, mongo)

      def create(rc: RoutingContext): Task[Response] =
        for {
          schedule        <- Crud.create[Schedule]("Schedules", rc, config, jwt, logger, micrometer, mongo)
          userId          <- Crud.userId(rc, config, jwt)
          _               <- scheduler.deploy(schedule, userId)
          response        =  Response.Empty()
        } yield response


      def update(rc: RoutingContext): Task[Response] =
        for {
          schedule        <- Crud.update[Schedule] ("Schedules", rc, config, jwt, logger, micrometer, mongo)
          userId          <- Crud.userId(rc, config, jwt)
          _               <- scheduler.deploy(schedule, userId)
          response        =  Response.Empty()
        } yield response


      def delete(rc: RoutingContext): Task[Response] =
        for {
          schedule        <- Crud.get[Schedule]("Schedules", rc, config, jwt, logger, micrometer, mongo).map(_.get)
          userId          <- Crud.userId(rc, config, jwt)
          _               <- scheduler.undeploy(schedule, userId)
          _               <- Crud.delete[Schedule]("Schedules", rc, config, jwt, logger, micrometer, mongo)
          response        =  Response.Empty()
        } yield response
    }
  }
}