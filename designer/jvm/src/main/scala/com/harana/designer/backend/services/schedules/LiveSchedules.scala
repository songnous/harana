package com.harana.designer.backend.services.schedules

import com.harana.designer.backend.services.Crud
import com.harana.designer.backend.services.schedules.Schedules.Service
import com.harana.designer.backend.services.schedules.argo.ArgoScheduler
import com.harana.id.jwt.modules.jwt.JWT
import com.harana.modules.core.config.Config
import com.harana.modules.core.logger.Logger
import com.harana.modules.core.micrometer.Micrometer
import com.harana.modules.mongo.{Mongo, convertToBson}
import com.harana.modules.vertx.models.Response
import com.harana.sdk.shared.models.common.{Background, Status, Visibility}
import com.harana.sdk.shared.models.flow.Flow
import com.harana.sdk.shared.models.schedules.{Action, Event, EventMode, Schedule, ScheduleExecution, ScheduleExecutionStatus, ScheduleExecutionSummary}
import com.harana.sdk.shared.utils.Random
import io.circe.syntax._
import io.vertx.ext.web.RoutingContext
import org.mongodb.scala.bson.Document
import org.mongodb.scala.model.Filters
import zio.{Task, UIO, ZLayer}

import java.time.Instant
import scala.jdk.CollectionConverters.CollectionHasAsScala

object LiveSchedules {
  val layer = ZLayer.fromServices { (config: Config.Service,
                                     jwt: JWT.Service,
                                     logger: Logger.Service,
                                     micrometer: Micrometer.Service,
                                     mongo: Mongo.Service,
                                     scheduler: ArgoScheduler.Service) =>
    new Service {

      def actionTypes(rc: RoutingContext): Task[Response] = Task(Response.JSON(Action.types.asJson))

      def eventTypes(rc: RoutingContext): Task[Response] = Task(Response.JSON(Event.types.asJson))

      def list(rc: RoutingContext): Task[Response] = Crud.listResponse[Schedule]("Schedules", rc, config, jwt, logger, micrometer, mongo)

      def tags(rc: RoutingContext): Task[Response] = Crud.tagsResponse[Schedule]("Schedules", rc, config, jwt, logger, micrometer, mongo)

      def owners(rc: RoutingContext): Task[Response] = Crud.ownersResponse[Schedule]("Schedules", rc, config, jwt, logger, micrometer, mongo)

      def search(rc: RoutingContext): Task[Response] = Crud.searchResponse[Schedule]("Schedules", rc, config, jwt, logger, micrometer, mongo)

      def get(rc: RoutingContext): Task[Response] = Crud.getResponse[Schedule]("Schedules", rc, config, jwt, logger, micrometer, mongo)

      def create(rc: RoutingContext): Task[Response] =
        for {
          schedule <- Crud.create[Schedule]("Schedules", rc, config, jwt, logger, micrometer, mongo)
          userId <- Crud.userId(rc, config, jwt)
          //          _               <- scheduler.deploy(schedule, userId)
          response = Response.Empty()
        } yield response


      def update(rc: RoutingContext): Task[Response] =
        for {
          schedule <- Crud.update[Schedule]("Schedules", rc, config, jwt, logger, micrometer, mongo)
          userId <- Crud.userId(rc, config, jwt)
          //          _               <- scheduler.deploy(schedule, userId)
          response = Response.Empty()
        } yield response


      def delete(rc: RoutingContext): Task[Response] =
        for {
          schedule <- Crud.get[Schedule]("Schedules", rc, config, jwt, logger, micrometer, mongo).map(_.get)
          userId <- Crud.userId(rc, config, jwt)
          //          _               <- scheduler.undeploy(schedule, userId)
          _ <- Crud.delete[Schedule]("Schedules", rc, config, jwt, logger, micrometer, mongo)
          response = Response.Empty()
        } yield response


      def history(rc: RoutingContext): Task[Response] =
        for {
          userId          <- Crud.userId(rc, config, jwt)
          size            <- Task(rc.queryParam("size").asScala.head.toInt).orElse(UIO(50))
          filter          <- UIO(Map("$or" -> Crud.creatorOrPublic(userId)))
          executions      <- mongo.findEquals[ScheduleExecution]("ScheduleExecutions", filter, Some("finished", false), Some(size)).onError(e => logger.error(e.prettyPrint))
          scheduleIds     =  executions.map(_.scheduleId)
          schedules       <- mongo.find[Schedule]("Schedules", Filters.in("id", scheduleIds: _*), limit = Some(size))
          combined        =  executions.map(se => (se, schedules.find(_.id == se.scheduleId).get))
          response        =  Response.JSON(combined.asJson)
        } yield response


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

          se1 = ScheduleExecution(s1.id, date1, Some(date2), ScheduleExecutionStatus.Executing, Some(userId), Instant.now, None, Instant.now, Random.long, Status.Active, Visibility.Owner, 1L, Set(), Map())
          se2 = ScheduleExecution(s1.id, date2, None, ScheduleExecutionStatus.PendingExecution, Some(userId), Instant.now, None, Instant.now, Random.long, Status.Active, Visibility.Owner, 1L, Set(), Map())
          se3 = ScheduleExecution(s1.id, date3, Some(date4), ScheduleExecutionStatus.Failed, Some(userId), Instant.now, None, Instant.now, Random.long, Status.Active, Visibility.Owner, 1L, Set(), Map())
          se4 = ScheduleExecution(s1.id, date4, None, ScheduleExecutionStatus.Killed, Some(userId), Instant.now, None, Instant.now, Random.long, Status.Active, Visibility.Owner, 1L, Set(), Map())
          se5 = ScheduleExecution(s1.id, date5, None, ScheduleExecutionStatus.Cancelled, Some(userId), Instant.now, None, Instant.now, Random.long, Status.Active, Visibility.Owner, 1L, Set(), Map())
          se6 = ScheduleExecution(s1.id, date6, None, ScheduleExecutionStatus.Initialised, Some(userId), Instant.now, None, Instant.now, Random.long, Status.Active, Visibility.Owner, 1L, Set(), Map())
          se7 = ScheduleExecution(s1.id, date7, None, ScheduleExecutionStatus.Paused, Some(userId), Instant.now, None, Instant.now, Random.long, Status.Active, Visibility.Owner, 1L, Set(), Map())
          se8 = ScheduleExecution(s1.id, date8, None, ScheduleExecutionStatus.Executing, Some(userId), Instant.now, None, Instant.now, Random.long, Status.Active, Visibility.Owner, 1L, Set(), Map())
          se9 = ScheduleExecution(s1.id, date4, Some(date9), ScheduleExecutionStatus.Succeeded, Some(userId), Instant.now, None, Instant.now, Random.long, Status.Active, Visibility.Owner, 1L, Set(), Map())
          se10 = ScheduleExecution(s2.id, date10, None, ScheduleExecutionStatus.PendingCancellation, Some(userId), Instant.now, None, Instant.now, Random.long, Status.Active, Visibility.Owner, 1L, Set(), Map())
          se11 = ScheduleExecution(s2.id, date11, Some(date13), ScheduleExecutionStatus.Succeeded, Some(userId), Instant.now, None, Instant.now, Random.long, Status.Active, Visibility.Owner, 1L, Set(), Map())
          se12 = ScheduleExecution(s2.id, date12, None, ScheduleExecutionStatus.TimedOut, Some(userId), Instant.now, None, Instant.now, Random.long, Status.Active, Visibility.Owner, 1L, Set(), Map())
          se13 = ScheduleExecution(s3.id, date13, None, ScheduleExecutionStatus.Executing, Some(userId), Instant.now, None, Instant.now, Random.long, Status.Active, Visibility.Owner, 1L, Set(), Map())


          _               <- mongo.insert[Schedule]("Schedules", s1.copy(recentExecutions = List(se1, se2, se3, se4, se5, se6, se7, se8, se9).map(ScheduleExecutionSummary.apply)))
          _               <- mongo.insert[Schedule]("Schedules", s2.copy(recentExecutions = List(se10, se11, se12, se13).map(ScheduleExecutionSummary.apply)))
          _               <- mongo.insert[Schedule]("Schedules", s3)
          _               <- mongo.insert[Schedule]("Schedules", s4)

          _               <- mongo.insert[ScheduleExecution]("ScheduleExecutions", se1)
          _               <- mongo.insert[ScheduleExecution]("ScheduleExecutions", se2)
          _               <- mongo.insert[ScheduleExecution]("ScheduleExecutions", se3)
          _               <- mongo.insert[ScheduleExecution]("ScheduleExecutions", se4)
          _               <- mongo.insert[ScheduleExecution]("ScheduleExecutions", se5)
          _               <- mongo.insert[ScheduleExecution]("ScheduleExecutions", se6)
          _               <- mongo.insert[ScheduleExecution]("ScheduleExecutions", se7)
          _               <- mongo.insert[ScheduleExecution]("ScheduleExecutions", se8)
          _               <- mongo.insert[ScheduleExecution]("ScheduleExecutions", se9)
          _               <- mongo.insert[ScheduleExecution]("ScheduleExecutions", se10)
          _               <- mongo.insert[ScheduleExecution]("ScheduleExecutions", se11)
          _               <- mongo.insert[ScheduleExecution]("ScheduleExecutions", se12)
          _               <- mongo.insert[ScheduleExecution]("ScheduleExecutions", se13)

          response = Response.Empty()

        } yield response
      }
    }
  }
}