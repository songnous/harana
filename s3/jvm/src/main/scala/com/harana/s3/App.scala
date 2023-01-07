package com.harana.s3

import com.harana.modules.core.app.{App => CoreApp}
import com.harana.modules.vertx.Vertx
import com.harana.modules.vertx.models.Route
import io.vertx.core.http.HttpMethod._

object App extends CoreApp {

  def routes = List(
    Route("/*",                                               GET,        rc => handle(rc, GET)),
    Route("/*",                                               PUT,        rc => handle(rc, PUT)),
    Route("/*",                                               POST,       rc => handle(rc, POST)),
    Route("/*",                                               OPTIONS,    rc => handle(rc, OPTIONS)),
    Route("/*",                                               DELETE,     rc => handle(rc, DELETE))
  )


    // MAKE SURE TO CREATE LOCAL.ROOT


  def startup =
    for {
      domain                <- env("harana_domain")
      _                     <- Vertx.startHttpServer(s"s3.$domain", routes = routes).provideLayer(vertx).toManaged_.useForever
    } yield ()
}