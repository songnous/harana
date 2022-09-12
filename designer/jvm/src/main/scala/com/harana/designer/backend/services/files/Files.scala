package com.harana.designer.backend.services.files

import com.harana.modules.vertx.models.Response
import io.vertx.ext.web.RoutingContext
import zio.macros.accessible
import zio.{Has, Task}

@accessible
object Files {
   type Files = Has[Files.Service]

  trait Service {
    def list(rc: RoutingContext): Task[Response]

    def tags(rc: RoutingContext): Task[Response]

    def search(rc: RoutingContext): Task[Response]

    def info(rc: RoutingContext): Task[Response]

    def updateInfo(rc: RoutingContext): Task[Response]

    def delete(rc: RoutingContext): Task[Response]

    def createDirectory(rc: RoutingContext): Task[Response]

    def upload(rc: RoutingContext): Task[Response]

    def download(rc: RoutingContext): Task[Response]

    def preview(rc: RoutingContext): Task[Response]

    def copy(rc: RoutingContext): Task[Response]

    def move(rc: RoutingContext): Task[Response]

    def duplicate(rc: RoutingContext): Task[Response]

    def compress(rc: RoutingContext): Task[Response]

    def decompress(rc: RoutingContext): Task[Response]

    def startFileSharing(rc: RoutingContext): Task[Response]

    def stopFileSharing(rc: RoutingContext): Task[Response]

    def isFileSharingEnabled(rc: RoutingContext): Task[Response]

    def startRemoteLogin(rc: RoutingContext): Task[Response]

    def stopRemoteLogin(rc: RoutingContext): Task[Response]

    def isRemoteLoginEnabled(rc: RoutingContext): Task[Response]
  }
}