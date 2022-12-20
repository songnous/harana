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
import com.harana.sdk.shared.models.common.{Status, Visibility}
import com.harana.sdk.shared.models.schedules._
import com.harana.sdk.shared.utils.Random
import io.circe.syntax._
import io.vertx.ext.web.RoutingContext
import org.mongodb.scala.model.Filters
import zio.{Task, UIO, ZLayer}

import java.time.{Duration, Instant}
import scala.jdk.CollectionConverters.CollectionHasAsScala

object LiveSchedules {
  val layer = ZLayer.fromServices { (config: Config.Service,
                                     jwt: JWT.Service,
                                     logger: Logger.Service,
                                     micrometer: Micrometer.Service,
                                     mongo: Mongo.Service,
                                     scheduler: ArgoScheduler.Service) =>
    new Service {
      def list(rc: RoutingContext): Task[Response] = Crud.listResponse[Schedule]("Schedules", rc, config, jwt, logger, micrometer, mongo)
      def tags(rc: RoutingContext): Task[Response] = Crud.tagsResponse[Schedule]("Schedules", rc, config, jwt, logger, micrometer, mongo)
      def owners(rc: RoutingContext): Task[Response] = Crud.ownersResponse[Schedule]("Schedules", rc, config, jwt, logger, micrometer, mongo)
      def search(rc: RoutingContext): Task[Response] = Crud.searchResponse[Schedule]("Schedules", rc, config, jwt, logger, micrometer, mongo)
      def get(rc: RoutingContext): Task[Response] = Crud.getResponse[Schedule]("Schedules", rc, config, jwt, logger, micrometer, mongo)

      def create(rc: RoutingContext): Task[Response] =
        for {
          schedule        <- Crud.create[Schedule]("Schedules", rc, config, jwt, logger, micrometer, mongo)
          userId          <- Crud.userId(rc, config, jwt)
          //          _               <- scheduler.deploy(schedule, userId)
          response        = Response.Empty()
        } yield response


      def update(rc: RoutingContext): Task[Response] =
        for {
          schedule        <- Crud.update[Schedule]("Schedules", rc, config, jwt, logger, micrometer, mongo)
          userId          <- Crud.userId(rc, config, jwt)
          //          _               <- scheduler.deploy(schedule, userId)
          response        = Response.Empty()
        } yield response


      def delete(rc: RoutingContext): Task[Response] =
        for {
          schedule        <- Crud.get[Schedule]("Schedules", rc, config, jwt, logger, micrometer, mongo).map(_.get)
          userId          <- Crud.userId(rc, config, jwt)
          //          _               <- scheduler.undeploy(schedule, userId)
          _               <- Crud.delete[Schedule]("Schedules", rc, config, jwt, logger, micrometer, mongo)
          response        = Response.Empty()
        } yield response


      def trigger(rc: RoutingContext): Task[Response] =
        Task(Response.Empty())


      def enable(rc: RoutingContext): Task[Response] =
        Task(Response.Empty())


      def disable(rc: RoutingContext): Task[Response] =
        Task(Response.Empty())


      def setup(rc: RoutingContext): Task[Response] = {
        val date1 = Instant.parse("2022-11-01T12:00:00.00Z")
        val date2 = Instant.parse("2022-11-01T18:30:00.00Z")
        val date3 = Instant.parse("2022-11-02T12:00:00.00Z")
        val date4 = Instant.parse("2022-11-02T18:30:00.00Z")
        val date5 = Instant.parse("2022-11-03T12:00:00.00Z")
        val date6 = Instant.parse("2022-11-03T18:30:00.00Z")
        val date7 = Instant.parse("2022-11-04T12:00:00.00Z")
        val date8 = Instant.parse("2022-11-05T18:30:00.00Z")
        val date9 = Instant.parse("2022-11-05T12:00:00.00Z")
        val date10 = Instant.parse("2022-11-06T18:30:00.00Z")
        val date11 = Instant.parse("2022-11-06T12:00:00.00Z")
        val date12 = Instant.parse("2022-11-06T12:30:00.00Z")
        val date13 = Instant.parse("2022-11-07T18:00:00.00Z")

        for {
          userId         <- Crud.userId(rc, config, jwt)
          _               <- logger.info(">>> Setting up scheudles")

          s1 = Schedule("Schedule One", "", List(), EventMode.All, List(), List(), List(), Some(userId), Visibility.Owner, None, Set())
          s2 = Schedule("Schedule Two", "", List(), EventMode.All, List(), List(), List(), Some(userId), Visibility.Owner, None, Set()).copy(status = Status.Paused)
          s3 = Schedule("Schedule Three", "", List(), EventMode.All, List(), List(), List(), Some(userId), Visibility.Owner, None, Set())
          s4 = Schedule("Schedule Four", "", List(), EventMode.All, List(), List(), List(), Some(userId), Visibility.Owner, None, Set())

          se1 = ScheduleExecution(Random.long, s1.id, date1, Some(date2), ScheduleExecutionStatus.Executing)
          se2 = ScheduleExecution(Random.long, s1.id, date2, None, ScheduleExecutionStatus.PendingExecution)
          se3 = ScheduleExecution(Random.long, s1.id, date3, Some(date4), ScheduleExecutionStatus.Failed)
          se4 = ScheduleExecution(Random.long, s1.id, date4, None, ScheduleExecutionStatus.Killed)
          se5 = ScheduleExecution(Random.long, s1.id, date5, None, ScheduleExecutionStatus.Cancelled)
          se6 = ScheduleExecution(Random.long, s1.id, date6, None, ScheduleExecutionStatus.Initialised)
          se7 = ScheduleExecution(Random.long, s1.id, date7, None, ScheduleExecutionStatus.Paused)
          se8 = ScheduleExecution(Random.long, s1.id, date8, None, ScheduleExecutionStatus.Executing)
          se9 = ScheduleExecution(Random.long, s1.id, date4, Some(date9), ScheduleExecutionStatus.Succeeded)
          se10 = ScheduleExecution(Random.long, s2.id, date10, None, ScheduleExecutionStatus.PendingCancellation)
          se11 = ScheduleExecution(Random.long, s2.id, date11, Some(date13), ScheduleExecutionStatus.Succeeded)
          se12 = ScheduleExecution(Random.long, s2.id, date12, None, ScheduleExecutionStatus.TimedOut)
          se13 = ScheduleExecution(Random.long, s3.id, date13, None, ScheduleExecutionStatus.Executing)


          _               <- mongo.insert[Schedule]("Schedules", s1.copy(executions = List(se1, se2, se3, se4, se5, se6, se7, se8, se9)))
          _               <- mongo.insert[Schedule]("Schedules", s2.copy(executions = List(se10, se11, se12, se13)))
          _               <- mongo.insert[Schedule]("Schedules", s3)
          _               <- mongo.insert[Schedule]("Schedules", s4)

          response = Response.Empty()

        } yield response
      }
    }
  }
}