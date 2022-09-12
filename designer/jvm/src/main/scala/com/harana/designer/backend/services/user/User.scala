package com.harana.designer.backend.services.user

import com.harana.modules.vertx.models.Response
import io.vertx.ext.web.RoutingContext
import zio.macros.accessible
import zio.{Has, Task}

@accessible
object User {
   type User = Has[User.Service]

  trait Service {
    def onboard(rc: RoutingContext): Task[Response]

    def logout(rc: RoutingContext): Task[Response]

    def renewToken(rc: RoutingContext): Task[Response]

    def preferences(rc: RoutingContext): Task[Response]

    def savePreferences(rc: RoutingContext): Task[Response]

    def settings(rc: RoutingContext): Task[Response]

    def saveSettings(rc: RoutingContext): Task[Response]

    def startSession(rc: RoutingContext): Task[Response]

    def extendSession(rc: RoutingContext): Task[Response]
  }
}