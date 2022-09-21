package com.harana.sdk.shared.models.schedules

import com.harana.sdk.shared.models.data.DataSource.DataSourceId
import com.harana.sdk.shared.models.flow.Flow.FlowId
import com.harana.sdk.shared.models.schedules.Schedule.ScheduleId
import io.circe.derivation.{deriveDecoder, deriveEncoder}
import io.circe.syntax._
import io.circe.{Decoder, Encoder}

import scala.scalajs.reflect.annotation.EnableReflectiveInstantiation

sealed trait Event

@EnableReflectiveInstantiation
object Event {

  case class CalendarInterval(interval: Option[String] = None,
                              timeZone: Option[String] = None,
                              exclusionDates: List[String] = List()) extends Event

  case class CalendarSchedule(schedule: Option[String] = None,
                              timeZone: Option[String] = None,
                              exclusionDates: List[String] = List()) extends Event

  case class DataSyncStarted(dataSourceId: Option[DataSourceId] = None) extends Event

  case class DataSyncCompleted(dataSourceId: Option[DataSourceId] = None) extends Event

  case class DataSyncFailed(dataSourceId: Option[DataSourceId] = None,
                            errorMessage: Option[String] = None) extends Event

  case class FileCreated(path: Option[String] = None,
                         pathRegex: Option[String] = None) extends Event

  case class FileModified(path: Option[String] = None,
                          pathRegex: Option[String] = None) extends Event

  case class FileDeleted(path: Option[String] = None,
                         pathRegex: Option[String] = None) extends Event

  case class FlowStarted(flowId: Option[FlowId] = None) extends Event

  case class FlowCompleted(flowId: Option[FlowId] = None) extends Event

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

  case class ScheduleCompleted(scheduleId: Option[ScheduleId] = None) extends Event

  case class ScheduleFailed(scheduleId: Option[ScheduleId] = None,
                            errorMessage: Option[String] = None) extends Event

  case class Webhook(endpoint: Option[String] = None,
                     port: Option[Long] = None) extends Event

  def withName(name: String): Event = name match {
    case "CalendarInterval" => CalendarInterval()
    case "CalendarSchedule" => CalendarSchedule()
    case "DataSyncStarted" => DataSyncStarted()
    case "DataSyncCompleted" => DataSyncCompleted()
    case "DataSyncFailed" => DataSyncFailed()
    case "FileCreated" => FileCreated()
    case "FileModified" => FileModified()
    case "FileDeleted" => FileDeleted()
    case "FlowStarted" => FlowStarted()
    case "FlowCompleted" => FlowCompleted()
    case "FlowFailed" => FlowFailed()
    case "Github" => Github()
    case "Gitlab" => Gitlab()
    case "ScheduleStarted" => ScheduleStarted()
    case "ScheduleCompleted" => ScheduleCompleted()
    case "ScheduleFailed" => ScheduleFailed()
    case "Webhook" => Webhook()
  }

  val types = List(
    CalendarInterval,
    CalendarSchedule,
    DataSyncStarted,
    DataSyncCompleted,
    DataSyncFailed,
    FileCreated,
    FileModified,
    FileDeleted,
    FlowStarted,
    FlowCompleted,
    FlowFailed,
    Github,
    Gitlab,
    ScheduleStarted,
    ScheduleCompleted,
    ScheduleFailed,
    Webhook).map(_.getClass.getSimpleName.replace("$", ""))

  implicit val decoder = Decoder.instance[Event] { c =>
    val content = c.downField("event").success.get
    c.downField("type").as[String].getOrElse(throw new Exception("Event type not found")) match {
      case "CalendarInterval" => deriveDecoder[CalendarInterval].apply(content)
      case "CalendarSchedule" => deriveDecoder[CalendarSchedule].apply(content)
      case "DataSyncStarted" => deriveDecoder[DataSyncStarted].apply(content)
      case "DataSyncCompleted" => deriveDecoder[DataSyncCompleted].apply(content)
      case "DataSyncFailed" => deriveDecoder[DataSyncFailed].apply(content)
      case "FileCreated" => deriveDecoder[FileCreated].apply(content)
      case "FileModified" => deriveDecoder[FileModified].apply(content)
      case "FileDeleted" => deriveDecoder[FileDeleted].apply(content)
      case "FlowStarted" => deriveDecoder[FlowStarted].apply(content)
      case "FlowCompleted" => deriveDecoder[FlowCompleted].apply(content)
      case "FlowFailed" => deriveDecoder[FlowFailed].apply(content)
      case "Github" => deriveDecoder[Github].apply(content)
      case "Gitlab" => deriveDecoder[Gitlab].apply(content)
      case "ScheduleStarted" => deriveDecoder[ScheduleStarted].apply(content)
      case "ScheduleCompleted" => deriveDecoder[ScheduleCompleted].apply(content)
      case "ScheduleFailed" => deriveDecoder[ScheduleFailed].apply(content)
      case "Webhook" => deriveDecoder[Webhook].apply(content)
    }
  }

  implicit val encoder = Encoder.instance[Event] { event =>
    val eventType = event.getClass.getSimpleName
    val json = eventType match {
      case "CalendarInterval" => deriveEncoder[CalendarInterval].apply(event.asInstanceOf[CalendarInterval])
      case "CalendarSchedule" => deriveEncoder[CalendarSchedule].apply(event.asInstanceOf[CalendarSchedule])
      case "DataSyncStarted" => deriveEncoder[DataSyncStarted].apply(event.asInstanceOf[DataSyncStarted])
      case "DataSyncCompleted" => deriveEncoder[DataSyncCompleted].apply(event.asInstanceOf[DataSyncCompleted])
      case "DataSyncFailed" => deriveEncoder[DataSyncFailed].apply(event.asInstanceOf[DataSyncFailed])
      case "FileCreated" => deriveEncoder[FileCreated].apply(event.asInstanceOf[FileCreated])
      case "FileModified" => deriveEncoder[FileModified].apply(event.asInstanceOf[FileModified])
      case "FileDeleted" => deriveEncoder[FileDeleted].apply(event.asInstanceOf[FileDeleted])
      case "FlowStarted" => deriveEncoder[FlowStarted].apply(event.asInstanceOf[FlowStarted])
      case "FlowCompleted" => deriveEncoder[FlowCompleted].apply(event.asInstanceOf[FlowCompleted])
      case "FlowFailed" => deriveEncoder[FlowFailed].apply(event.asInstanceOf[FlowFailed])
      case "Github" => deriveEncoder[Github].apply(event.asInstanceOf[Github])
      case "ScheduleStarted" => deriveEncoder[ScheduleStarted].apply(event.asInstanceOf[ScheduleStarted])
      case "ScheduleCompleted" => deriveEncoder[ScheduleCompleted].apply(event.asInstanceOf[ScheduleCompleted])
      case "ScheduleFailed" => deriveEncoder[ScheduleFailed].apply(event.asInstanceOf[ScheduleFailed])
      case "Webhook" => deriveEncoder[Webhook].apply(event.asInstanceOf[Webhook])
    }
    io.circe.Json.obj("type" -> eventType.asJson, "value" -> json)
  }
}