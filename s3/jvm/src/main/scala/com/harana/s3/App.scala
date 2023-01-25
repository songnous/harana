package com.harana.s3

import com.harana.modules.core.app.{App => CoreApp}
import com.harana.modules.core.{Layers => CoreLayers}
import com.harana.modules.vertx.Vertx
import com.harana.modules.vertx.models.Route
import com.harana.modules.vertx.models.RouteHandler._
import com.harana.modules.{Layers => ModuleLayers}
import com.harana.s3.services.router.LiveRouter
import com.harana.s3.services.server._
import io.vertx.core.http.HttpMethod._
import zio.{UIO, _}
import zio.clock.Clock

object App extends CoreApp {

  val router = (Clock.live ++ CoreLayers.standard ++ ModuleLayers.awsS3 ++ ModuleLayers.file ++ ModuleLayers.ohc) >>> LiveRouter.layer
  val server = (Clock.live ++ CoreLayers.standard ++ CoreLayers.cache ++ ModuleLayers.mongo ++ router ++ ModuleLayers.vertx) >>> LiveServer.layer

  def routes = List(
    Route("/_admin/route",          GET,         Standard(rc => Server.listRoutes(rc).provideLayer(server))),
    Route("/_admin/route/:id",      DELETE,      Standard(rc => Server.deleteRoute(rc).provideLayer(server))),
    Route("/_admin/route/:id",      PUT,         Standard(rc => Server.updateRoute(rc).provideLayer(server))),
    Route("/_admin/route/:id",      POST,        Standard(rc => Server.createRoute(rc).provideLayer(server))),
    Route("/_admin/sample",         GET,         Standard(rc => Server.sampleData(rc).provideLayer(server)))
  )

  def startup =
    for {
      domain                <- env("harana_domain")
      _                     <- logInfo(s"Starting s3 on: s3.$domain")
      _                     <- Server.syncRoutes.provideLayer(server)
      _                     <- Vertx.startHttpServer(s"s3.$domain",
                                routes = routes,
                                defaultHandler = Some(Stream((rc, stream, pump) => Server.s3Request(rc, stream, pump).provideLayer(server)))
                               ).provideLayer(ModuleLayers.vertx).toManaged_.useForever
    } yield ()

  def shutdown = UIO.unit
}