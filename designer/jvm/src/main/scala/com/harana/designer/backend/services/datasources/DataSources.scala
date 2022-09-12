package com.harana.designer.backend.services.datasources

import com.harana.modules.vertx.models.Response
import io.vertx.ext.web.RoutingContext
import zio.macros.accessible
import zio.{Has, Task}

@accessible
object DataSources {
   type DataSources = Has[DataSources.Service]

  trait Service {
    def list(rc: RoutingContext): Task[Response]

    def listWithTypeId(rc: RoutingContext): Task[Response]

    def tags(rc: RoutingContext): Task[Response]

    def owners(rc: RoutingContext): Task[Response]

    def search(rc: RoutingContext): Task[Response]

    def get(rc: RoutingContext): Task[Response]

    def delete(rc: RoutingContext): Task[Response]

    def create(rc: RoutingContext): Task[Response]

    def update(rc: RoutingContext): Task[Response]

    def typesWithDirection(rc: RoutingContext): Task[Response]

    def typeWithId(rc: RoutingContext): Task[Response]

    def sync(rc: RoutingContext): Task[Response]
  }
}