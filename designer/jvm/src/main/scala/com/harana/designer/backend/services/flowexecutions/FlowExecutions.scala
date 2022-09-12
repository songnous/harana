package com.harana.designer.backend.services.flowexecutions

import com.harana.modules.vertx.models.Response
import io.vertx.ext.web.RoutingContext
import zio.macros.accessible
import zio.{Has, Task}

@accessible
object FlowExecutions {
  type FlowExecutions = Has[FlowExecutions.Service]

  trait Service {
    def availableFlows(rc: RoutingContext): Task[Response]

    def cancelledFlows(rc: RoutingContext): Task[Response]

    def outputVariables(rc: RoutingContext): Task[Response]

    def logs(rc: RoutingContext): Task[Response]

    def progress(rc: RoutingContext): Task[Response]

    def dataSources(rc: RoutingContext): Task[Response]
  }
}