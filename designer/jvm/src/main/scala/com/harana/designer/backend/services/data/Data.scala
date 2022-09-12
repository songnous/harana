package com.harana.designer.backend.services.data

import com.harana.modules.vertx.models.Response
import io.vertx.ext.web.RoutingContext
import zio.macros.accessible
import zio.{Has, Task}

@accessible
object Data {
   type Data = Has[Data.Service]

  trait Service {
    def list(rc: RoutingContext): Task[Response]

    def tags(rc: RoutingContext): Task[Response]

    def search(rc: RoutingContext): Task[Response]

    def info(rc: RoutingContext): Task[Response]

    def delete(rc: RoutingContext): Task[Response]

    def update(rc: RoutingContext): Task[Response]

    def createDirectory(rc: RoutingContext): Task[Response]

    def upload(rc: RoutingContext): Task[Response]

    def download(rc: RoutingContext): Task[Response]

    def copy(rc: RoutingContext): Task[Response]

    def move(rc: RoutingContext): Task[Response]
  }
}