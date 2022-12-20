package com.harana.designer.backend.services.schedules

import com.harana.modules.vertx.models.Response
import io.vertx.ext.web.RoutingContext
import zio.macros.accessible
import zio.{Has, Task}

@accessible
object Schedules {
   type Schedules = Has[Schedules.Service]

  trait Service {
    def list(rc: RoutingContext): Task[Response]

    def tags(rc: RoutingContext): Task[Response]

    def owners(rc: RoutingContext): Task[Response]

    def search(rc: RoutingContext): Task[Response]

    def get(rc: RoutingContext): Task[Response]

    def delete(rc: RoutingContext): Task[Response]

    def create(rc: RoutingContext): Task[Response]

    def update(rc: RoutingContext): Task[Response]

    def setup(rc: RoutingContext): Task[Response]

    def trigger(rc: RoutingContext): Task[Response]

    def enable(rc: RoutingContext): Task[Response]

    def disable(rc: RoutingContext): Task[Response]
  }
}