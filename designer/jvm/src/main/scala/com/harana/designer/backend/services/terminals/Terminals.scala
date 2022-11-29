package com.harana.designer.backend.services.terminals

import com.harana.modules.vertx.models.Response
import io.vertx.ext.web.RoutingContext
import zio.macros.accessible
import zio.{Has, Task, UIO}

@accessible
object Terminals {
   type Terminals = Has[Terminals.Service]

  trait Service {

    def list(rc: RoutingContext): Task[Response]

    def get(rc: RoutingContext): Task[Response]

    def delete(rc: RoutingContext): Task[Response]

    def create(rc: RoutingContext): Task[Response]

    def update(rc: RoutingContext): Task[Response]

    def connect(rc: RoutingContext): Task[Response]

    def disconnect(rc: RoutingContext): Task[Response]

    def restart(rc: RoutingContext): Task[Response]

    def clear(rc: RoutingContext): Task[Response]

    def history(rc: RoutingContext): Task[Response]

    def shutdown: UIO[Unit]

    def startup: Task[Unit]
  }
}