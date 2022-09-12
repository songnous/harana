package com.harana.designer.backend.services.apps

import java.net.URI

import com.harana.modules.vertx.Vertx.WebSocketHeaders
import com.harana.modules.vertx.models.Response
import com.harana.modules.vertx.proxy.WSURI
import io.vertx.ext.web.RoutingContext
import zio.macros.accessible
import zio.{Has, Task}

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

    def start(rc: RoutingContext): Task[Response]

    def stop(rc: RoutingContext): Task[Response]

    def restart(rc: RoutingContext): Task[Response]

    def proxyHttp(rc: RoutingContext): Task[URI]

    def proxyWebsocket(headers: WebSocketHeaders): Task[WSURI]
  }
}