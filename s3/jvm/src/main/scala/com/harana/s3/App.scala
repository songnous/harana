package com.harana.s3

import com.harana.modules.core.app.{App => CoreApp}
import com.harana.modules.vertx.{LiveVertx, Vertx}
import com.harana.modules.vertx.models.Route
import io.vertx.core.http.HttpMethod._
import zio.blocking.Blocking
import com.harana.modules.{Layers => ModuleLayers}
import com.harana.modules.core.{Layers => CoreLayers}
import com.harana.s3.services.router.LiveRouter
import com.harana.s3.services.server._
import zio.UIO
import zio.clock.Clock

object App extends CoreApp {

  val router = (Clock.live ++ CoreLayers.standard ++ ModuleLayers.awsS3 ++ ModuleLayers.jasyncfio ++ ModuleLayers.ohc) >>> LiveRouter.layer
  val s3server = (CoreLayers.standard ++ CoreLayers.cache ++ router ++ ModuleLayers.vertx) >>> LiveServer.layer

  def startup =
    for {
      domain                <- env("harana_domain")
      _                     <- Vertx.startHttpServer(s"s3.$domain", routes = List(
                                Route("/*",     GET,        rc => Server.handle(rc, GET).provideLayer(s3server)),
                                Route("/*",     PUT,        rc => Server.handle(rc, PUT).provideLayer(s3server)),
                                Route("/*",     POST,       rc => Server.handle(rc, POST).provideLayer(s3server)),
                                Route("/*",     OPTIONS,    rc => Server.handle(rc, OPTIONS).provideLayer(s3server)),
                                Route("/*",     DELETE,     rc => Server.handle(rc, DELETE).provideLayer(s3server))
                               )).provideLayer(ModuleLayers.vertx).toManaged_.useForever
    } yield ()

  def shutdown = UIO.unit
}