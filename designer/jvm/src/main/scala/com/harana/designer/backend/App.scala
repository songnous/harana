package com.harana.designer.backend

import com.harana.Layers
import com.harana.designer.backend.services.apps.{Apps, LiveApps}
import com.harana.designer.backend.services.data.LiveData
import com.harana.designer.backend.services.datasets.{DataSets, LiveDataSets}
import com.harana.designer.backend.services.datasources.{DataSources, LiveDataSources}
import com.harana.designer.backend.services.events.{Events, LiveEvents}
import com.harana.designer.backend.services.files.{Files, LiveFiles}
import com.harana.designer.backend.services.flowexecutions.{FlowExecutions, LiveFlowExecutions}
import com.harana.designer.backend.services.flows.{Flows, LiveFlows}
import com.harana.designer.backend.services.help.{Help, LiveHelp}
import com.harana.designer.backend.services.schedules.argo.LiveArgoScheduler
import com.harana.designer.backend.services.schedules.{LiveSchedules, Schedules}
import com.harana.designer.backend.services.setup.{LiveSetup, Setup}
import com.harana.designer.backend.services.system.{LiveSystem, System}
import com.harana.designer.backend.services.terminals.{LiveTerminals, Terminals}
import com.harana.designer.backend.services.user.{LiveUser, User}
import com.harana.id.jwt.modules.jwt.JWT
import com.harana.id.jwt.{Layers => JWTLayers}
import com.harana.modules.core.app.{App => CoreApp}
import com.harana.modules.core.micrometer.{LiveMicrometer, Micrometer}
import com.harana.modules.core.{Layers => CoreLayers}
import com.harana.modules.mongo.Mongo
import com.harana.modules.vertx.models.{Route, _}
import com.harana.modules.vertx.{LiveVertx, Vertx}
import com.harana.modules.vfs.LiveVfs
import com.harana.sdk.shared.models.common.{User => DesignerUser}
import com.harana.sdk.shared.models.flow.FlowExecution
import com.harana.sdk.shared.models.flow.catalog.Catalog
import com.harana.sdk.shared.models.flow.execution.spark.ExecutionStatus
import com.harana.sdk.shared.models.jwt.DesignerClaims
import io.vertx.core.http.HttpMethod._
import io.vertx.ext.web.RoutingContext
import zio.blocking.Blocking
import zio.clock.Clock
import zio.duration.durationInt
import zio.{Schedule, Task}

import java.time.Instant
import scala.util.Try

object App extends CoreApp {

    // FIXME: Airbyte requires CoreLayers.standard when it shouldn't
  val dataSources = (CoreLayers.standard ++ (CoreLayers.standard >>> Layers.airbyte) ++ JWTLayers.jwt ++ Layers.mongo) >>> LiveDataSources.layer
  val dataSets = (CoreLayers.standard ++ JWTLayers.jwt ++ Layers.mongo) >>> LiveDataSets.layer
  val system = (CoreLayers.standard ++ Clock.live ++ JWTLayers.jwt ++ Layers.mongo ++ Layers.vertx) >>> LiveSystem.layer
  val execution = (CoreLayers.standard ++ Layers.mongo) >>> LiveFlowExecutions.layer
  val events = (CoreLayers.standard ++ JWTLayers.jwt ++ Layers.mongo) >>> LiveEvents.layer
  val flows = (Layers.kubernetes ++ CoreLayers.standard ++ JWTLayers.jwt ++ Layers.mongo) >>> LiveFlows.layer
  val setup = (CoreLayers.standard ++ Clock.live ++ Layers.kubernetes ++ Layers.mongo) >>> LiveSetup.layer
  val vertx = (Blocking.live ++ CoreLayers.standard ++ CoreLayers.cache) >>> LiveVertx.layer
  val user = (CoreLayers.standard ++ Layers.aws ++ JWTLayers.jwt ++ Layers.kubernetes ++ Layers.mongo ++ vertx) >>> LiveUser.layer
  val apps = (CoreLayers.standard ++ Clock.live ++ JWTLayers.jwt ++ Layers.kubernetes ++ Layers.mongo ++ vertx) >>> LiveApps.layer
  val data = (CoreLayers.standard ++ Layers.alluxioFs ++ JWTLayers.jwt ++ vertx) >>> LiveData.layer
  val files = (CoreLayers.standard ++ JWTLayers.jwt ++ Layers.kubernetes ++ vertx ++ LiveVfs.layer) >>> LiveFiles.layer

  val argoScheduler = (CoreLayers.standard ++ Layers.argo ++ Layers.kubernetes) >>> LiveArgoScheduler.layer
  val schedules = (CoreLayers.standard ++ JWTLayers.jwt ++ argoScheduler ++ Layers.mongo ++ vertx) >>> LiveSchedules.layer
  val terminals = (CoreLayers.standard ++ JWTLayers.jwt ++ Layers.kubernetes ++ Layers.mongo ++ vertx) >>> LiveTerminals.layer
  val help = (CoreLayers.standard ++ JWTLayers.jwt ++ Layers.mongo) >>> LiveHelp.layer

  private def routes = List(
    Route("/",                                                GET,      rc => homePage(rc), isBlocking = false),
    Route("/welcome",                                         GET,      rc => homePage(rc, true), isBlocking = false),
    Route("/apps*",                                           GET,      rc => homePage(rc), isBlocking = false),
    Route("/data*",                                           GET,      rc => homePage(rc), isBlocking = false),
    Route("/files*",                                          GET,      rc => homePage(rc), isBlocking = false),
    Route("/flows*",                                          GET,      rc => homePage(rc), isBlocking = false),
    Route("/help*",                                           GET,      rc => homePage(rc), isBlocking = false),
    Route("/schedules*",                                      GET,      rc => homePage(rc), isBlocking = false),
    Route("/settings*",                                       GET,      rc => homePage(rc), isBlocking = false),
    Route("/terminal*",                                       GET,      rc => homePage(rc), isBlocking = false),
    Route("/test*",                                           GET,      rc => homePage(rc), isBlocking = false),

    Route("/health",                                          GET,      rc => System.health(rc).provideLayer(system)),

    Route("/system/error",                                    POST,     rc => System.error(rc).provideLayer(system)),
//    Route("/system/events",                                   GET,      rc => Events.stream(rc).provideLayer(system)),

  // Apps
    Route("/api/apps/start/:id",                              GET,      rc => Apps.start(rc).provideLayer(apps)),
    Route("/api/apps/restart/:id",                            GET,      rc => Apps.restart(rc).provideLayer(apps)),
    Route("/api/apps/stop/:id",                               GET,      rc => Apps.stop(rc).provideLayer(apps)),
    Route("/api/apps/search/:query",                          GET,      rc => Apps.search(rc).provideLayer(apps)),
    Route("/api/apps/tags",                                   GET,      rc => Apps.tags(rc).provideLayer(apps)),
    Route("/api/apps",                                        GET,      rc => Apps.list(rc).provideLayer(apps)),
    Route("/api/apps",                                        POST,     rc => Apps.create(rc).provideLayer(apps)),
    Route("/api/apps",                                        PUT,      rc => Apps.update(rc).provideLayer(apps)),
    Route("/api/apps/:id",                                    GET,      rc => Apps.get(rc).provideLayer(apps)),
    Route("/api/apps/:id",                                    DELETE,   rc => Apps.delete(rc).provideLayer(apps)),

    // Datasets
    Route("/api/datasets/search/:query",                      GET,      rc => DataSets.search(rc).provideLayer(dataSets)),
    Route("/api/datasets/tags",                               GET,      rc => DataSets.tags(rc).provideLayer(dataSets)),
    Route("/api/datasets",                                    GET,      rc => DataSets.list(rc).provideLayer(dataSets)),
    Route("/api/datasets",                                    POST,     rc => DataSets.create(rc).provideLayer(dataSets)),
    Route("/api/datasets",                                    PUT,      rc => DataSets.update(rc).provideLayer(dataSets)),
    Route("/api/datasets/:id",                                GET,      rc => DataSets.get(rc).provideLayer(dataSets)),
    Route("/api/datasets/:id",                                DELETE,   rc => DataSets.delete(rc).provideLayer(dataSets)),
    Route("/api/datasets/:id/queryable",                      DELETE,   rc => DataSets.delete(rc).provideLayer(dataSets)),

    // Data Sources
    Route("/api/datasources/search/:query",                   GET,      rc => DataSources.search(rc).provideLayer(dataSources)),
    Route("/api/datasources/tags",                            GET,      rc => DataSources.tags(rc).provideLayer(dataSources)),
    Route("/api/datasources",                                 GET,      rc => DataSources.list(rc).provideLayer(dataSources)),
    Route("/api/datasources",                                 POST,     rc => DataSources.create(rc).provideLayer(dataSources)),
    Route("/api/datasources",                                 PUT,      rc => DataSources.update(rc).provideLayer(dataSources)),
    Route("/api/datasources/:id",                             GET,      rc => DataSources.get(rc).provideLayer(dataSources)),
    Route("/api/datasources/:id",                             DELETE,   rc => DataSources.delete(rc).provideLayer(dataSources)),
    Route("/api/datasources/type/:typeId",                    GET,      rc => DataSources.listWithTypeId(rc).provideLayer(dataSources)),

    Route("/api/datasources/types/direction/:direction",      GET,      rc => DataSources.typesWithDirection(rc).provideLayer(dataSources)),
    Route("/api/datasources/types/:id",                       GET,      rc => DataSources.typeWithId(rc).provideLayer(dataSources)),

    // Events
    Route("/api/events",                                      GET,      rc => Events.list(rc).provideLayer(events)),
    Route("/api/events/:id",                                  DELETE,   rc => Events.delete(rc).provideLayer(events)),

    // Files
    Route("/api/files/search/:query",                         GET,      rc => Files.search(rc).provideLayer(files)),
    Route("/api/files/info",                                  GET,      rc => Files.info(rc).provideLayer(files)),
    Route("/api/files/info",                                  POST,     rc => Files.updateInfo(rc).provideLayer(files)),
    Route("/api/files/tags",                                  GET,      rc => Files.tags(rc).provideLayer(files)),
    Route("/api/files/directory",                             POST,     rc => Files.createDirectory(rc).provideLayer(files)),
    Route("/api/files/download",                              GET,      rc => Files.download(rc).provideLayer(files)),
    Route("/api/files/preview",                               GET,      rc => Files.preview(rc).provideLayer(files)),
    Route("/api/files/copy",                                  GET,      rc => Files.copy(rc).provideLayer(files)),
    Route("/api/files/move",                                  GET,      rc => Files.move(rc).provideLayer(files)),
    Route("/api/files/duplicate",                             GET,      rc => Files.duplicate(rc).provideLayer(files)),
    Route("/api/files/compress",                              GET,      rc => Files.compress(rc).provideLayer(files)),
    Route("/api/files/decompress",                            GET,      rc => Files.decompress(rc).provideLayer(files)),
    Route("/api/files",                                       GET,      rc => Files.list(rc).provideLayer(files)),
    Route("/api/files",                                       POST,     rc => Files.upload(rc).provideLayer(files)),
    Route("/api/files",                                       DELETE,   rc => Files.delete(rc).provideLayer(files)),

    // Flows
    Route("/api/flows/search/:query",                         GET,      rc => Flows.search(rc).provideLayer(flows)),
    Route("/api/flows/tags",                                  GET,      rc => Flows.tags(rc).provideLayer(flows)),
    Route("/api/flows/start/:flowId",                         PUT,      rc => Flows.start(rc).provideLayer(flows)),
    Route("/api/flows/stop/:flowExecutionId",                 PUT,      rc => Flows.stop(rc).provideLayer(flows)),
    Route("/api/flows/progress/:id",                          POST,     rc => Flows.updateProgress(rc).provideLayer(flows)),
    Route("/api/flows",                                       GET,      rc => Flows.list(rc).provideLayer(flows)),
    Route("/api/flows",                                       POST,     rc => Flows.create(rc).provideLayer(flows)),
    Route("/api/flows",                                       PUT,      rc => Flows.update(rc).provideLayer(flows)),
    Route("/api/flows/:id",                                   GET,      rc => Flows.get(rc).provideLayer(flows)),
    Route("/api/flows/:id",                                   DELETE,   rc => Flows.delete(rc).provideLayer(flows)),

    // Flow Executions
    Route("/api/flows/executions/flows/variables/:flowId",    GET,      rc => FlowExecutions.outputVariables(rc).provideLayer(execution)),
    Route("/api/flows/executions/pending",                    GET,      rc => FlowExecutions.cancelledFlows(rc).provideLayer(execution)),
    Route("/api/flows/executions/available",                  GET,      rc => FlowExecutions.availableFlows(rc).provideLayer(execution)),
    Route("/api/flows/executions/datasources/:flowId",        GET,      rc => FlowExecutions.dataSources(rc).provideLayer(execution)),
    Route("/api/flows/executions/logs/:flowId",               GET,      rc => FlowExecutions.logs(rc).provideLayer(execution)),
    Route("/api/flows/executions/progress/:flowId",           GET,      rc => FlowExecutions.progress(rc).provideLayer(execution)),

    // Schedules
    Route("/api/schedules",                                   GET,      rc => Schedules.list(rc).provideLayer(schedules)),
    Route("/api/schedules",                                   POST,     rc => Schedules.create(rc).provideLayer(schedules)),
    Route("/api/schedules",                                   PUT,      rc => Schedules.update(rc).provideLayer(schedules)),
    Route("/api/schedules/:id",                               GET,      rc => Schedules.get(rc).provideLayer(schedules)),
    Route("/api/schedules/:id",                               DELETE,   rc => Schedules.delete(rc).provideLayer(schedules)),
    Route("/api/schedules/search/:query",                     GET,      rc => Schedules.search(rc).provideLayer(schedules)),
    Route("/api/schedules/tags",                              GET,      rc => Schedules.tags(rc).provideLayer(schedules)),
    Route("/api/schedules/trigger/:id",                       GET,      rc => Schedules.trigger(rc).provideLayer(schedules)),
    Route("/api/schedules/enable/:id",                        GET,      rc => Schedules.enable(rc).provideLayer(schedules)),
    Route("/api/schedules/disable/:id",                       GET,      rc => Schedules.disable(rc).provideLayer(schedules)),

    Route("/api/help",                                        GET,      rc => Help.list(rc).provideLayer(help)),
    Route("/api/help/:id",                                    GET,      rc => Help.get(rc).provideLayer(help)),

    Route("/api/content/:id",                                 GET,      rc => System.content(rc).provideLayer(system)),

    Route("/api/terminals",                                   GET,      rc => Terminals.list(rc).provideLayer(terminals)),
    Route("/api/terminals",                                   POST,     rc => Terminals.create(rc).provideLayer(terminals)),
    Route("/api/terminals",                                   PUT,      rc => Terminals.update(rc).provideLayer(terminals)),
    Route("/api/terminals/:id",                               GET,      rc => Terminals.get(rc).provideLayer(terminals)),
    Route("/api/terminals/:id",                               DELETE,   rc => Terminals.delete(rc).provideLayer(terminals)),
    Route("/api/terminals/:id/connect/:rows/:cols",           GET,      rc => Terminals.connect(rc).provideLayer(terminals)),
    Route("/api/terminals/:id/disconnect",                    GET,      rc => Terminals.disconnect(rc).provideLayer(terminals)),
    Route("/api/terminals/:id/restart",                       GET,      rc => Terminals.restart(rc).provideLayer(terminals)),
    Route("/api/terminals/:id/clear",                         GET,      rc => Terminals.clear(rc).provideLayer(terminals)),
    Route("/api/terminals/:id/history",                       GET,      rc => Terminals.history(rc).provideLayer(terminals)),

    Route("/api/test/logs",                                   GET,      rc => User.testLogs(rc).provideLayer(user)),

    // User
    Route("/api/user/onboard",                                GET,      rc => User.onboard(rc).provideLayer(user)),
    Route("/api/user/preferences",                            GET,      rc => User.preferences(rc).provideLayer(user)),
    Route("/api/user/preferences",                            POST,     rc => User.savePreferences(rc).provideLayer(user)),
    Route("/api/user/settings",                               GET,      rc => User.settings(rc).provideLayer(user)),
    Route("/api/user/setup",                                  GET,      rc => Schedules.setup(rc).provideLayer(schedules))
  )


  def startup =
    for {
      _                     <- exposePendingExecutionsMetric.repeat(Schedule.spaced(10.second).forever).provideLayer(Clock.live).fork

      actions               =  Catalog.actionsByIdMap.size

      domain                <- env("harana_domain")

//      _                     <- Mongo.createQueue[Event]("EventsGlobal").provideLayer(Layers.mongo)

      _                     <- Terminals.startup.provideLayer(terminals)
      _                     <- System.createIndexes.provideLayer(system)
      _                     <- Vertx.startHttpServer(
                                s"designer.$domain",
                                Some(s"designer-proxy.$domain"),
                                routes,
                                proxyMapping = Some(rc => Apps.proxyHttp(rc).option.provideLayer(apps)),
                                webSocketProxyMapping = Some(headers => Apps.proxyWebsocket(headers).provideLayer(apps)),
                                eventBusInbound = List(".*"),
                                eventBusOutbound = List(".*")
                              ).provideLayer(vertx).toManaged_.useForever
    } yield ()


  def exposePendingExecutionsMetric =
    for {
        pending             <- Mongo.findEquals[FlowExecution]("FlowExecutions", Map("executionStatus" -> ExecutionStatus.PendingExecution.toString)).provideLayer(Layers.mongo)
        _                   <- Micrometer.gauge[Integer]("designer_flows_pending", Map(), pending.size).provideLayer(LiveMicrometer.layer)
    } yield ()


  def shutdown =
    for {
      _                     <- Terminals.shutdown.provideLayer(terminals)
      _                     <- Vertx.close.provideLayer(vertx)
    } yield ()


  def homePage(rc: RoutingContext, onboard: Boolean = false): Task[Response] =
    for {
      domain                    <- env("harana_domain")
      gaMeasurementId           <- secret("google-analytics-measurement-id")
      i18nProperties            <- System.i18n("en").provideLayer(system)
      claims                    <- claims(rc).option.map(_.flatten)
      response                  <- claims match {
                                      case Some(claims) =>
                                        for {
                                          _               <- Mongo.updateFields("Users", claims.userId, Map("lastSession" -> Instant.now().toString)).provideLayer(Layers.mongo)
                                          _               <- Task.when(onboard)(Setup.createApps(claims).provideLayer(setup))
                                          parameters      =  Map(
                                                              "authDomain"        -> s"id.$domain",
                                                              "debug"             -> "false",
                                                              "domain"            -> domain,
                                                              "gaMeasurementId"   -> gaMeasurementId,
                                                              "initialJwt"        -> rc.request.getCookie("jwt").getValue,
                                                              "i18nProperties"    -> i18nProperties,
                                                              "proxyDomain"       -> s"designer-proxy.$domain",
                                                              "userId"            -> claims.userId)
                                          template        =  Response.Template("templates/index.html", parameters = parameters)
                                        } yield template

                                      case None =>
                                        for {
                                          _               <- Task(rc.response.removeCookie("jwt"))
                                          redirect        =  Response.Redirect(s"https://harana.com/login?id_domain=id.$domain")
                                        } yield redirect
                                    }
    } yield response

 
  def claims(rc: RoutingContext): Task[Option[DesignerClaims]] =
    for {
      jwtValue    <- Task.fromTry(Try(rc.request.getCookie("jwt").getValue))
      claims      <- JWT.claims[DesignerClaims](jwtValue).provideLayer(JWTLayers.jwt)
      user        <- Mongo.findOne[DesignerUser]("Users", Map("id" -> claims.userId)).onError(e => logError(e.prettyPrint)).option.provideLayer(Layers.mongo)
      result      =  if (user.flatten.isDefined && claims.issued.isAfter(user.flatten.get.updated)) Some(claims) else None
    } yield result

}