package com.harana.sdk.shared.models.schedules

import com.harana.sdk.shared.models.data.DataSource.DataSourceId
import com.harana.sdk.shared.models.flow.Flow.FlowId
import com.harana.sdk.shared.models.schedules.Action.DataSync
import com.harana.sdk.shared.models.schedules.Schedule.ScheduleId
import io.circe.derivation.{deriveDecoder, deriveEncoder}
import io.circe.syntax._
import io.circe.{Decoder, Encoder}

import scala.scalajs.reflect.annotation.EnableReflectiveInstantiation

sealed trait Event

@EnableReflectiveInstantiation
object Event {

  type EventId = String

  case class CalendarInterval(interval: Option[String] = None,
                              timeZone: Option[String] = None,
                              exclusionDates: List[String] = List()) extends Event

  case class CalendarSchedule(schedule: Option[String] = None,
                              timeZone: Option[String] = None,
                              exclusionDates: List[String] = List()) extends Event

  case class DataSyncStarted(dataSourceId: Option[DataSourceId] = None) extends Event

  case class DataSyncFinished(dataSourceId: Option[DataSourceId] = None) extends Event

  case class DataSyncFailed(dataSourceId: Option[DataSourceId] = None,
                            errorMessage: Option[String] = None) extends Event

  case class FileCreated(path: Option[String] = None,
                         pathRegex: Option[String] = None) extends Event

  case class FileModified(path: Option[String] = None,
                          pathRegex: Option[String] = None) extends Event

  case class FileDeleted(path: Option[String] = None,
                         pathRegex: Option[String] = None) extends Event

  case class FlowStarted(flowId: Option[FlowId] = None) extends Event

  case class FlowFinished(flowId: Option[FlowId] = None) extends Event

  case class FlowFailed(flowId: Option[FlowId] = None,
                        errorMessage: Option[String] = None) extends Event

  case class Github(owner: Option[String] = None,
                    repository: Option[String] = None,
                    endpoint: Option[String] = None,
                    port: Option[Long] = None,
                    url: Option[String] = None,
                    event: Option[String] = None,
                    apiSecret: Option[String] = None,
                    webhookSecret: Option[String] = None) extends Event

  case class Gitlab(projectId: Option[String] = None,
                    endpoint: Option[String] = None,
                    port: Option[Long] = None,
                    url: Option[String] = None,
                    event: Option[String] = None,
                    apiSecret: Option[String] = None,
                    baseUrl: Option[String] = None) extends Event

  case class ScheduleStarted(scheduleId: Option[ScheduleId] = None) extends Event

  case class ScheduleFinished(scheduleId: Option[ScheduleId] = None) extends Event

  case class ScheduleFailed(scheduleId: Option[ScheduleId] = None,
                            errorMessage: Option[String] = None) extends Event

  case class Webhook(host: Option[String] = None,
                     port: Option[Long] = None) extends Event


  val types = List[Event](
    new CalendarInterval,
    new CalendarSchedule,
    new DataSyncStarted,
    new DataSyncFinished,
    new DataSyncFailed,
    new FileCreated,
    new FileModified,
    new FileDeleted,
    new FlowStarted,
    new FlowFinished,
    new FlowFailed,
//    new Github,
//    new Gitlab,
    new ScheduleStarted,
    new ScheduleFinished,
    new ScheduleFailed)

  def newWithName(name: String) = types.find(_.getClass.getSimpleName == name).get
  val typesByName = types.map(_.getClass.getSimpleName.replace("$", ""))


  implicit val decoder = Decoder.instance[Event] { c =>
    val content = c.downField("value").success.get
    c.downField("type").as[String].getOrElse(throw new Exception("Event type not found")) match {
      case "CalendarInterval"    => deriveDecoder[CalendarInterval].apply(content)
      case "CalendarSchedule"    => deriveDecoder[CalendarSchedule].apply(content)
      case "DataSyncStarted"     => deriveDecoder[DataSyncStarted].apply(content)
      case "DataSyncFinished"    => deriveDecoder[DataSyncFinished].apply(content)
      case "DataSyncFailed"      => deriveDecoder[DataSyncFailed].apply(content)
      case "FileCreated"         => deriveDecoder[FileCreated].apply(content)
      case "FileModified"        => deriveDecoder[FileModified].apply(content)
      case "FileDeleted"         => deriveDecoder[FileDeleted].apply(content)
      case "FlowStarted"         => deriveDecoder[FlowStarted].apply(content)
      case "FlowFinished"        => deriveDecoder[FlowFinished].apply(content)
      case "FlowFailed"          => deriveDecoder[FlowFailed].apply(content)
      case "Github"              => deriveDecoder[Github].apply(content)
      case "Gitlab"              => deriveDecoder[Gitlab].apply(content)
      case "ScheduleStarted"     => deriveDecoder[ScheduleStarted].apply(content)
      case "ScheduleFinished"    => deriveDecoder[ScheduleFinished].apply(content)
      case "ScheduleFailed"      => deriveDecoder[ScheduleFailed].apply(content)
    }
  }

  implicit val encoder = Encoder.instance[Event] { event =>
    val eventType = event.getClass.getSimpleName
    val json = eventType match {
      case "CalendarInterval"    => deriveEncoder[CalendarInterval].apply(event.asInstanceOf[CalendarInterval])
      case "CalendarSchedule"    => deriveEncoder[CalendarSchedule].apply(event.asInstanceOf[CalendarSchedule])
      case "DataSyncStarted"     => deriveEncoder[DataSyncStarted].apply(event.asInstanceOf[DataSyncStarted])
      case "DataSyncFinished"    => deriveEncoder[DataSyncFinished].apply(event.asInstanceOf[DataSyncFinished])
      case "DataSyncFailed"      => deriveEncoder[DataSyncFailed].apply(event.asInstanceOf[DataSyncFailed])
      case "FileCreated"         => deriveEncoder[FileCreated].apply(event.asInstanceOf[FileCreated])
      case "FileModified"        => deriveEncoder[FileModified].apply(event.asInstanceOf[FileModified])
      case "FileDeleted"         => deriveEncoder[FileDeleted].apply(event.asInstanceOf[FileDeleted])
      case "FlowStarted"         => deriveEncoder[FlowStarted].apply(event.asInstanceOf[FlowStarted])
      case "FlowFinished"        => deriveEncoder[FlowFinished].apply(event.asInstanceOf[FlowFinished])
      case "FlowFailed"          => deriveEncoder[FlowFailed].apply(event.asInstanceOf[FlowFailed])
      case "Github"              => deriveEncoder[Github].apply(event.asInstanceOf[Github])
      case "Gitlab"              => deriveEncoder[Gitlab].apply(event.asInstanceOf[Gitlab])
      case "ScheduleStarted"     => deriveEncoder[ScheduleStarted].apply(event.asInstanceOf[ScheduleStarted])
      case "ScheduleFinished"    => deriveEncoder[ScheduleFinished].apply(event.asInstanceOf[ScheduleFinished])
      case "ScheduleFailed"      => deriveEncoder[ScheduleFailed].apply(event.asInstanceOf[ScheduleFailed])

    }
    io.circe.Json.obj("type" -> eventType.asJson, "value" -> json)
  }
}