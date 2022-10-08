package com.harana.designer.backend.services.schedules.argo

import com.harana.designer.backend.App.env
import com.harana.designer.backend.services.schedules.argo.ArgoScheduler.Service
import com.harana.modules.argo.{Argo, SecretKeySelector, SecureHeader, ValueFromSource}
import com.harana.modules.argo.events.{EventSource, Sensor}
import com.harana.modules.argo.events.EventSource.{EventSource, WatchPathConfig}
import com.harana.modules.argo.events.Sensor.{EventDependency, Sensor, Trigger, TriggerTemplate}
import com.harana.modules.argo.events.Trigger._
import com.harana.modules.core.config.Config
import com.harana.modules.core.logger.Logger
import com.harana.modules.core.micrometer.Micrometer
import com.harana.modules.kubernetes.Kubernetes
import com.harana.sdk.shared.models.common.User.UserId
import com.harana.sdk.shared.models.schedules.{Action, Event, Schedule}
import skuber.LabelSelector
import skuber.LabelSelector.IsEqualRequirement
import zio.{Task, ZIO, ZLayer}

object LiveArgoScheduler {
  val layer = ZLayer.fromServices { (argo: Argo.Service,
                                     config: Config.Service,
                                     kubernetes: Kubernetes.Service,
                                     logger: Logger.Service,
                                     micrometer: Micrometer.Service) => new Service {

//    def deploy(schedule: Schedule, userId: UserId): Task[Unit] =
//      for {
//        namespace       <- config.string("designer.flows.namespace")
//        client          <- kubernetes.newClient
//        eventBusName    = ""
//        project         = ""

//        eventSources    <- argoEventSources(eventBusName, null)
//        sensors         <- argoSensors(null)
//        _               <- ZIO.foreachPar_(eventSources)(e => argo.createOrUpdateEventSource(namespace, e, Some(client)))
//        _               <- ZIO.foreachPar_(sensors)(s => argo.createOrUpdateSensor(namespace, s, Some(client)))

//        _               <- kubernetes.close(client)
//      } yield ()


//    def undeploy(schedule: Schedule, userId: UserId): Task[Unit] =
//      for {
//        namespace       <- config.string("designer.flows.namespace")
//        client          <- kubernetes.newClient
//        eventBusName    = ""
//        project         = ""

//        eventSources    <- argoEventSources(eventBusName, null)
//        sensors         <- argoSensors(null)
//        _               <- ZIO.foreachPar_(eventSources.map(_.metadata.name))(e => argo.deleteEventSource(namespace, e, Some(client)))
//        _               <- ZIO.foreachPar_(sensors.map(_.metadata.name))(s => argo.deleteSensor(namespace, s, Some(client)))

//        _               <- kubernetes.close(client)
//      } yield ()
//

    private def _createArgo(userId: String, schedule: Schedule): Task[Unit] =
      for {
        eventBusName    <- config.string("events.busName")
        domain          <- env("harana_domain")
        authToken       <- config.secret("harana-token")

        triggers        =  _triggers(userId, authToken, domain, schedule.actions)
        eventSources    =  _eventSources(userId, schedule.id, eventBusName, s"https://events.$domain", schedule.events)
        _               <- Task.foreach_(eventSources)(es => argo.createOrUpdateEventSource("harana-events", es._2))
        _               <- argo.createOrUpdateSensor("harana-events", Sensor("name", Sensor.Spec(
          eventBusName = Some(eventBusName),
          dependencies = eventSources.zipWithIndex.map { case (es, index) => EventDependency("name", es._1, index.toString) },
          triggers = triggers
        )))
      } yield ()


    private def _deleteArgo(schedule: Schedule): Task[Unit] =
      for {
        client          <- kubernetes.newClient
        label           =  LabelSelector(IsEqualRequirement("scheduleId", schedule.id))
        _               <- kubernetes.deleteAllSelected[EventSource](client, "harana-events", label)
        _               <- kubernetes.deleteAllSelected[Sensor](client, "harana-events", label)
        _               <- kubernetes.close(client)
      } yield ()


    private def _eventSources(userId: String,
                              scheduleId: Schedule.ScheduleId,
                              eventBusName: String,
                              eventServerUrl: String,
                              events: List[Event]): List[(String, EventSource)] =

      events.zipWithIndex.map { case (event, index) => event match {

        case Event.CalendarInterval(interval, timeZone, exclusionDates) =>
          ("calendar", EventSource("", EventSource.Spec(eventBusName, calendar = Map(index.toString -> EventSource.Calendar(exclusionDates, interval, None, timeZone)))))

        case Event.CalendarSchedule(schedule, timeZone, exclusionDates) =>
          ("calendar", EventSource("", EventSource.Spec(eventBusName, calendar = Map(index.toString -> EventSource.Calendar(exclusionDates, None, schedule, timeZone)))))

        case Event.DataSyncStarted(dataSourceId) =>
          ("generic", EventSource("", EventSource.Spec(eventBusName, generic = Map(index.toString -> EventSource.Generic(eventServerUrl)))))

        case Event.DataSyncCompleted(dataSourceId) =>
          ("generic", EventSource("", EventSource.Spec(eventBusName, generic = Map(index.toString -> EventSource.Generic(eventServerUrl)))))

        case Event.DataSyncFailed(dataSourceId, errorMessage) =>
          ("generic", EventSource("", EventSource.Spec(eventBusName, generic = Map(index.toString -> EventSource.Generic(eventServerUrl)))))

        case Event.FileCreated(path, pathRegex) =>
          ("file", EventSource("", EventSource.Spec(eventBusName, file = Map(index.toString -> EventSource.File("Create", WatchPathConfig(path = path, pathRegexp = pathRegex))))))

        case Event.FileModified(path, pathRegex) =>
          ("file", EventSource("", EventSource.Spec(eventBusName, file = Map(index.toString -> EventSource.File("Write", WatchPathConfig(path = path, pathRegexp = pathRegex))))))

        case Event.FileDeleted(path, pathRegex) =>
          ("file", EventSource("", EventSource.Spec(eventBusName, file = Map(index.toString -> EventSource.File("Delete", WatchPathConfig(path = path, pathRegexp = pathRegex))))))

        case Event.FlowStarted(flowId) =>
          ("generic", EventSource("", EventSource.Spec(eventBusName, generic = Map(index.toString -> EventSource.Generic(eventServerUrl)))))

        case Event.FlowCompleted(flowId) =>
          ("generic", EventSource("", EventSource.Spec(eventBusName, generic = Map(index.toString -> EventSource.Generic(eventServerUrl)))))

        case Event.FlowFailed(flowId, errorMessage) =>
          ("generic", EventSource("", EventSource.Spec(eventBusName, generic = Map(index.toString -> EventSource.Generic(eventServerUrl)))))

        case Event.Github(owner, repository, endpoint, port, url, event, apiSecret, webhookSecret) =>
          ("github", EventSource("", EventSource.Spec(eventBusName, github = Map(index.toString -> EventSource.Github(owner.get, repository.get, endpoint.get, port.get, url, List(), apiSecret.get, null)))))

        case Event.Gitlab(projectId, endpoint, port, url, event, apiSecret, domain) =>
          ("gitlab", EventSource("", EventSource.Spec(eventBusName, gitlab = Map(index.toString -> EventSource.Gitlab(projectId.get, endpoint.get, port.get, url, event.get, apiSecret.get, null)))))

        case Event.ScheduleStarted(scheduleId) =>
          ("generic", EventSource("", EventSource.Spec(eventBusName, generic = Map(index.toString -> EventSource.Generic(eventServerUrl)))))

        case Event.ScheduleCompleted(scheduleId) =>
          ("generic", EventSource("", EventSource.Spec(eventBusName, generic = Map(index.toString -> EventSource.Generic(eventServerUrl)))))

        case Event.ScheduleFailed(scheduleId, errorMessage) =>
          ("generic", EventSource("", EventSource.Spec(eventBusName, generic = Map(index.toString -> EventSource.Generic(eventServerUrl)))))

        case Event.Webhook(endpoint, port) =>
          ("webhook", EventSource("", EventSource.Spec(eventBusName, webhook = Map(index.toString -> EventSource.Webhook(endpoint.get, port.getOrElse(80))))))
      }}


    private def _triggers(userId: String,
                          authToken: String,
                          domain: String,
                          actions: List[Action]): List[Trigger] =

      actions.map {

        case Action.DataSync(dataSourceId) =>
          Trigger(TriggerTemplate("", http = httpTrigger(s"https://designer.$domain/data/sync/$dataSourceId", userId, authToken)))

        case Action.FileCompress(_, path) =>
          Trigger(TriggerTemplate("", http = httpTrigger(s"https://designer.$domain/data/files/compress?path=$path", userId, authToken)))

        case Action.FileCopy(_, fromPath, _, toPath) =>
          Trigger(TriggerTemplate("", http = httpTrigger(s"https://designer.$domain/data/files/copy?fromPath=$fromPath&toPath=$toPath", userId, authToken)))

        case Action.FileDecompress(_, path) =>
          Trigger(TriggerTemplate("", http = httpTrigger(s"https://designer.$domain/data/files/decompress?path=$path", userId, authToken)))

        case Action.FileDelete(_, path) =>
          Trigger(TriggerTemplate("", http = httpTrigger(s"https://designer.$domain/data/files$path=$path", userId, authToken, "DELETE")))

        case Action.FileDuplicate(_, path) =>
          Trigger(TriggerTemplate("", http = httpTrigger(s"https://designer.$domain/data/files/duplicate?path=$path", userId, authToken)))

        case Action.FileMkDir(_, path) =>
          Trigger(TriggerTemplate("", http = httpTrigger(s"https://designer.$domain/data/files/directory?path=$path", userId, authToken, "POST")))

        case Action.FileMove(_, fromPath, _, toPath) =>
          Trigger(TriggerTemplate("", http = httpTrigger(s"https://designer.$domain/data/files/move?fromPath=$fromPath&toPath=$toPath", userId, authToken)))

        case Action.FlowStart(flowId) =>
          Trigger(TriggerTemplate("", http = httpTrigger(s"https://designer.$domain/flow/start/$flowId", userId, authToken)))

        case Action.FlowStop(flowId) =>
          Trigger(TriggerTemplate("", http = httpTrigger(s"https://designer.$domain/flow/stop/$flowId", userId, authToken)))

        case Action.HttpRequest(url, method, parameters, headers) =>
          Trigger(TriggerTemplate("", http = Some(
            HttpTrigger(url = url.get, method = method, headers = headers)
          )))

        case Action.ScheduleEnable(scheduleId) =>
          Trigger(TriggerTemplate("", http = httpTrigger(s"https://designer.$domain/schedule/enable/$scheduleId", userId, authToken)))

        case Action.ScheduleDisable(scheduleId) =>
          Trigger(TriggerTemplate("", http = httpTrigger(s"https://designer.$domain/schedule/disable/$scheduleId", userId, authToken)))

      }


    private def httpTrigger(url: String, userId: String, authToken: String, method: String = "GET") =
      Some(HttpTrigger(
        url = url,
        headers = Map("user-id" -> userId),
        method = Some(method),
        secureHeaders = List(
          SecureHeader("auth-token", ValueFromSource(secretKeyRef = Some(SecretKeySelector(key = Some("value"), name = Some("auth-token")))))
        )
      ))
  }}
}