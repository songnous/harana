package com.harana.designer.backend.services.settings

import com.harana.designer.backend.services.settings.Settings.Service
import com.harana.modules.core.config.Config
import com.harana.modules.vertx.models.Response
import com.harana.modules.core.logger.Logger
import com.harana.modules.core.micrometer.Micrometer
import io.vertx.ext.web.RoutingContext
import zio.{Task, ZLayer}

object LiveSettings {
  val layer = ZLayer.fromServices { (config: Config.Service,
                                     logger: Logger.Service,
                                     micrometer: Micrometer.Service) => new Service {

    def listPlans(rc: RoutingContext): Task[Response] = {
      null
    }

    def changePlan(rc: RoutingContext): Task[Response] = {
      null
    }

    def cancelPlan(rc: RoutingContext): Task[Response] = {
      null
    }

    def getUserProfile(rc: RoutingContext): Task[Response] = {
      null
    }

    def saveUserProfile(rc: RoutingContext): Task[Response] = {
      null
    }

    def listConnections(rc: RoutingContext): Task[Response] = {
      null
    }

    def deleteConnection(rc: RoutingContext): Task[Response] = {
      null
    }

    def saveConnection(rc: RoutingContext): Task[Response] = {
      null
    }

    def testConnection(rc: RoutingContext): Task[Response] = {
      null
    }

  }}
}