package com.harana.designer.backend.services.settings

import com.harana.modules.vertx.models.Response
import io.vertx.ext.web.RoutingContext
import zio.macros.accessible
import zio.{Has, Task}

@accessible
object Settings {
  type Settings = Has[Settings.Service]

  trait Service {
    def listPlans(rc: RoutingContext): Task[Response]

    def changePlan(rc: RoutingContext): Task[Response]

    def cancelPlan(rc: RoutingContext): Task[Response]

    def getUserProfile(rc: RoutingContext): Task[Response]

    def saveUserProfile(rc: RoutingContext): Task[Response]

    def listConnections(rc: RoutingContext): Task[Response]

    def deleteConnection(rc: RoutingContext): Task[Response]

    def saveConnection(rc: RoutingContext): Task[Response]

    def testConnection(rc: RoutingContext): Task[Response]
  }
}