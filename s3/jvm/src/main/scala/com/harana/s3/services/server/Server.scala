package com.harana.s3.services.server

import com.harana.modules.vertx.models.Response
import io.vertx.core.buffer.Buffer
import io.vertx.core.streams.Pump
import io.vertx.ext.reactivestreams.ReactiveWriteStream
import io.vertx.ext.web.RoutingContext
import zio.macros.accessible
import zio.{Has, Task, UIO}

@accessible
object Server {
  type Server = Has[Server.Service]

  trait Service {

    def s3Request(rc: RoutingContext, stream: ReactiveWriteStream[Buffer], streamPump: Pump): Task[Response]

    def createRoute(rc: RoutingContext): Task[Response]

    def deleteRoute(rc: RoutingContext): Task[Response]

    def updateRoute(rc: RoutingContext): Task[Response]

    def listRoutes(rc: RoutingContext): Task[Response]

    def syncRoutes: UIO[Unit]

    def sampleData(rc: RoutingContext): Task[Response]

  }
}