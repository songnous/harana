package com.harana.s3

import com.harana.modules.core.app.{App => CoreApp}
import com.harana.modules.core.{Layers => CoreLayers}
import com.harana.modules.vertx.Vertx
import com.harana.modules.{Layers => ModuleLayers}
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
      _                     <- logInfo(s"Starting s3 on: s3.$domain")
      _                     <- Vertx.startHttpServer(s"s3.$domain", routeHandler = Some(rc => Server.handle(rc).provideLayer(s3server)))
                                .provideLayer(ModuleLayers.vertx).toManaged_.useForever
    } yield ()

  def shutdown = UIO.unit
}