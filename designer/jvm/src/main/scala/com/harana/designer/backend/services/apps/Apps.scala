package com.harana.designer.backend.services.apps

import java.net.URI
import com.harana.modules.vertx.Vertx.WebSocketHeaders
import com.harana.modules.vertx.models.Response
import com.harana.modules.vertx.proxy.WSURI
import io.vertx.ext.web.RoutingContext
import zio.macros.accessible
import zio.{Has, Task, UIO}

@accessible
object Apps {
  type Apps = Has[Apps.Service]

  trait Service {
    def list(rc: RoutingContext): Task[Response]

    def tags(rc: RoutingContext): Task[Response]

    def owners(rc: RoutingContext): Task[Response]

    def search(rc: RoutingContext): Task[Response]

    def get(rc: RoutingContext): Task[Response]

    def delete(rc: RoutingContext): Task[Response]

    def create(rc: RoutingContext): Task[Response]

    def update(rc: RoutingContext): Task[Response]

    def updates(rc: RoutingContext): Task[Response]

    def connect(rc: RoutingContext): Task[Response]

    def disconnect(rc: RoutingContext): Task[Response]

    def restart(rc: RoutingContext): Task[Response]

    def proxyHttp(rc: RoutingContext): Task[URI]

    def proxyWebsocket(headers: WebSocketHeaders): Task[WSURI]

    def startup: Task[Unit]

    def shutdown: UIO[Unit]
  }
}