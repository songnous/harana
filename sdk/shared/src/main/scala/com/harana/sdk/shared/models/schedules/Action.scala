package com.harana.sdk.shared.models.schedules

import com.harana.sdk.shared.models.data.DataSource.DataSourceId
import Schedule.ScheduleId
import com.harana.sdk.shared.models.flow.Flow.FlowId
import io.circe.derivation.{deriveDecoder, deriveEncoder}
import io.circe.syntax._
import io.circe.{Decoder, Encoder}

import scala.scalajs.reflect.annotation.EnableReflectiveInstantiation

@EnableReflectiveInstantiation
sealed trait Action

object Action {

  type ActionId = String

  case class DataSync(dataSourceId: Option[DataSourceId] = None) extends Action

  case class ExecuteCommand(command: Option[String] = None) extends Action

  case class ExecuteScript(path: Option[String] = None) extends Action

  case class FileCompress(compressionType: Option[ActionCompressionType] = None,
                          path: Option[String] = None) extends Action

  case class FileCopy(fromFileSource: Option[ActionFileSource] = None,
                      fromPath: Option[String] = None,
                      toFileSource: Option[ActionFileSource] = None,
                      toPath: Option[String] = None) extends Action

  case class FileDecompress(compressionType: Option[ActionCompressionType] = None,
                            path: Option[String] = None) extends Action

  case class FileDelete(fileSource: Option[ActionFileSource] = None,
                        path: Option[String] = None) extends Action

  case class FileDuplicate(fileSource: Option[ActionFileSource] = None,
                           path: Option[String] = None) extends Action

  case class FileMkDir(fileSource: Option[ActionFileSource] = None,
                       path: Option[String] = None) extends Action

  case class FileMove(fromFileSource: Option[ActionFileSource] = None,
                      fromPath: Option[String] = None,
                      toFileSource: Option[ActionFileSource] = None,
                      toPath: Option[String] = None) extends Action

  case class FileRename(fileSource: Option[ActionFileSource] = None,
                        path: Option[String] = None,
                        newName: Option[String] = None) extends Action

  case class FlowStart(flowId: Option[FlowId] = None) extends Action

  case class FlowStop(flowId: Option[FlowId] = None) extends Action

  case class HttpRequest(url: Option[String] = None,
                         method: Option[String] = None,
                         parameters: Map[String, String] = Map(),
                         headers: Map[String, String] = Map()) extends Action

  case class ScheduleEnable(scheduleId: Option[ScheduleId] = None) extends Action

  case class ScheduleDisable(scheduleId: Option[ScheduleId] = None) extends Action

  case class ScheduleTrigger(scheduleId: Option[ScheduleId] = None) extends Action

  case class SendEmail(scheduleId: Option[ScheduleId] = None) extends Action

  case class SendSlackMessage(scheduleId: Option[ScheduleId] = None) extends Action

  val types = List[Action](
    new DataSync,
    new ExecuteCommand,
    new ExecuteScript,
//    new FileCompress,
//    new FileCopy,
//    new FileDecompress,
//    new FileDelete,
//    new FileDuplicate,
//    new FileMkDir,
//    new FileMove,
//    new FileRename,
    new FlowStart,
    new FlowStop,
    new HttpRequest,
    new ScheduleEnable,
    new ScheduleDisable,
    new ScheduleTrigger,
    new SendEmail,
    new SendSlackMessage,
  )

  def newWithName(name: String) = types.find(_.getClass.getSimpleName == name).get
  val typesByName = types.map(_.getClass.getSimpleName.replace("$", ""))

  implicit val decoder = Decoder.instance[Action] { c =>
    val content = c.downField("value").success.get
    c.downField("type").as[String].getOrElse(throw new Exception("Action type not found")) match {
      case "DataSync"          => deriveDecoder[DataSync].apply(content)
      case "ExecuteCommand"    => deriveDecoder[ExecuteCommand].apply(content)
      case "ExecuteScript"     => deriveDecoder[ExecuteScript].apply(content)
      case "FileCompress"      => deriveDecoder[FileCompress].apply(content)
      case "FileCopy"          => deriveDecoder[FileCopy].apply(content)
      case "FileDecompress"    => deriveDecoder[FileDecompress].apply(content)
      case "FileDelete"        => deriveDecoder[FileDelete].apply(content)
      case "FileDuplicate"     => deriveDecoder[FileDuplicate].apply(content)
      case "FileMkDir"         => deriveDecoder[FileMkDir].apply(content)
      case "FileMove"          => deriveDecoder[FileMove].apply(content)
      case "FileRename"        => deriveDecoder[FileRename].apply(content)
      case "FlowStart"         => deriveDecoder[FlowStart].apply(content)
      case "FlowStop"          => deriveDecoder[FlowStop].apply(content)
      case "HttpRequest"       => deriveDecoder[HttpRequest].apply(content)
      case "ScheduleEnable"    => deriveDecoder[ScheduleEnable].apply(content)
      case "ScheduleDisable"   => deriveDecoder[ScheduleDisable].apply(content)
      case "ScheduleTrigger"   => deriveDecoder[ScheduleTrigger].apply(content)
      case "SendEmail"         => deriveDecoder[SendEmail].apply(content)
      case "SendSlackMessage"  => deriveDecoder[SendSlackMessage].apply(content)
    }
  }

  implicit val encoder = Encoder.instance[Action] { action =>
    val actionType = action.getClass.getSimpleName
    val json = actionType match {
      case "DataSync"          => deriveEncoder[DataSync].apply(action.asInstanceOf[DataSync])
      case "ExecuteCommand"    => deriveEncoder[ExecuteCommand].apply(action.asInstanceOf[ExecuteCommand])
      case "ExecuteScript"     => deriveEncoder[ExecuteScript].apply(action.asInstanceOf[ExecuteScript])
      case "FileCompress"      => deriveEncoder[FileCompress].apply(action.asInstanceOf[FileCompress])
      case "FileCopy"          => deriveEncoder[FileCopy].apply(action.asInstanceOf[FileCopy])
      case "FileDecompress"    => deriveEncoder[FileDecompress].apply(action.asInstanceOf[FileDecompress])
      case "FileDelete"        => deriveEncoder[FileDelete].apply(action.asInstanceOf[FileDelete])
      case "FileDuplicate"     => deriveEncoder[FileDuplicate].apply(action.asInstanceOf[FileDuplicate])
      case "FileMkDir"         => deriveEncoder[FileMkDir].apply(action.asInstanceOf[FileMkDir])
      case "FileMove"          => deriveEncoder[FileMove].apply(action.asInstanceOf[FileMove])
      case "FileRename"        => deriveEncoder[FileRename].apply(action.asInstanceOf[FileRename])
      case "FlowStart"         => deriveEncoder[FlowStart].apply(action.asInstanceOf[FlowStart])
      case "FlowStop"          => deriveEncoder[FlowStop].apply(action.asInstanceOf[FlowStop])
      case "HttpRequest"       => deriveEncoder[HttpRequest].apply(action.asInstanceOf[HttpRequest])
      case "ScheduleEnable"    => deriveEncoder[ScheduleEnable].apply(action.asInstanceOf[ScheduleEnable])
      case "ScheduleDisable"   => deriveEncoder[ScheduleDisable].apply(action.asInstanceOf[ScheduleDisable])
      case "ScheduleTrigger"   => deriveEncoder[ScheduleTrigger].apply(action.asInstanceOf[ScheduleTrigger])
      case "SendEmail"         => deriveEncoder[SendEmail].apply(action.asInstanceOf[SendEmail])
      case "SendSlackMessage"  => deriveEncoder[SendSlackMessage].apply(action.asInstanceOf[SendSlackMessage])
    }
    io.circe.Json.obj("type" -> actionType.asJson, "value" -> json)
  }
}
