package com.harana.designer.backend.services.flows

import com.harana.designer.backend.services.Crud
import com.harana.designer.backend.services.flows.Flows.Service
import com.harana.id.jwt.modules.jwt.JWT
import com.harana.id.jwt.shared.models.DesignerClaims
import com.harana.modules.core.config.Config
import com.harana.modules.core.logger.Logger
import com.harana.modules.core.micrometer.Micrometer
import com.harana.modules.kubernetes.Kubernetes
import com.harana.modules.mongo.Mongo
import com.harana.modules.vertx.models.Response
import com.harana.sdk.shared.models.designer.flow.execution.ExecutionStatus
import com.harana.sdk.shared.models.flow.{Flow, FlowExecution}
import com.harana.sdk.shared.models.jwt.DesignerClaims
import io.circe.syntax._
import io.vertx.ext.web.RoutingContext
import zio.{Task, ZLayer}

import java.time.Instant

object LiveFlows {
  val layer = ZLayer.fromServices { (config: Config.Service,
                                     jwt: JWT.Service,
                                     kubernetes: Kubernetes.Service,
                                     logger: Logger.Service,
                                     micrometer: Micrometer.Service,
                                     mongo: Mongo.Service) => new Service {


    def list(rc: RoutingContext): Task[Response] = Crud.listResponse[Flow]("Flows", rc, config, jwt, logger, micrometer, mongo)
    def tags(rc: RoutingContext): Task[Response] = Crud.tagsResponse[Flow]("Flows", rc, config, jwt, logger, micrometer, mongo)
    def owners(rc: RoutingContext): Task[Response] = Crud.ownersResponse[Flow]("Flows", rc, config, jwt, logger, micrometer, mongo)
    def search(rc: RoutingContext): Task[Response] = Crud.searchResponse[Flow]("Flows", rc, config, jwt, logger, micrometer, mongo)
    def get(rc: RoutingContext): Task[Response] = Crud.getResponse[Flow]("Flows", rc, config, jwt, logger, micrometer, mongo)
    def delete(rc: RoutingContext): Task[Response] = Crud.deleteResponse[Flow]("Flows", rc, config, jwt, logger, micrometer, mongo)
    def create(rc: RoutingContext): Task[Response] = Crud.createResponse[Flow]("Flows", rc, config, jwt, logger, micrometer, mongo)
    def update(rc: RoutingContext): Task[Response] = Crud.updateResponse[Flow]("Flows", rc, config, jwt, logger, micrometer, mongo)


    def start(rc: RoutingContext): Task[Response] =
      for {
        userId              <- Crud.userId(rc, config, jwt)
        jwt                 <- jwt.claims[DesignerClaims](rc)
        flowId              <- Task(rc.pathParam("flowId"))
        response            =  Response.Empty
      } yield response


    def stop(rc: RoutingContext): Task[Response] =
      for {
        userId              <- Crud.userId(rc, config, jwt)
        flowExecutionId     <- Task(rc.pathParam("flowExecutionId"))

        flowExecution       <- mongo.findOne[FlowExecution]("FlowExecutions", Map("id" -> flowExecutionId)).map(_.head)
        newFlowExecution    =  flowExecution.copy(
                                actionExecutions = flowExecution.actionExecutions.map(_.copy(executionStatus = ExecutionStatus.Cancelled)),
                                executionStatus = ExecutionStatus.Cancelled,
                                updated = Instant.now
                               )

        _                   <- mongo.update[FlowExecution]("FlowExecutions", newFlowExecution)

        response            =  Response.JSON(newFlowExecution.asJson)
      } yield response


    def updateProgress(rc: RoutingContext): Task[Response] = {
      null
    }

    def actionTypes(rc: RoutingContext): Task[Response] =
      for {
        flowType            <- Task(rc.pathParam("flowType"))
        response            =  Response.Empty()
      } yield response

  }}
}