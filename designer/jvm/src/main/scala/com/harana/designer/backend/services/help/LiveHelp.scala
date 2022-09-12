package com.harana.designer.backend.services.help

import com.harana.designer.backend.services.help.Help.Service
import com.harana.id.jwt.modules.jwt.JWT
import com.harana.modules.mongo.Mongo
import com.harana.modules.vertx.models.Response
import com.harana.modules.core.config.Config
import com.harana.modules.core.logger.Logger
import com.harana.modules.core.micrometer.Micrometer
import com.harana.sdk.shared.models.common.HelpCategory
import io.vertx.ext.web.RoutingContext
import io.circe.syntax._
import io.circe.yaml.parser._
import io.circe.parser.decode
import zio.{Task, UIO, ZIO, ZLayer}

import java.util.concurrent.atomic.AtomicReference

object LiveHelp {
  private val helpRef = new AtomicReference[List[HelpCategory]](List())

  val layer = ZLayer.fromServices { (config: Config.Service,
                                     jwt: JWT.Service,
                                     logger: Logger.Service,
                                     micrometer: Micrometer.Service,
                                     mongo: Mongo.Service) => new Service {

    def list(rc: RoutingContext): Task[Response] =
      Task(Response.Empty())
////      if (helpRef.get.nonEmpty)
////        Task(Response.JSON(helpRef.get.asJson))
////      else
//        for {
//          source  <- UIO(scala.io.Source.fromFile("public/help/list.yml"))
//          lines   <- Task(try source.mkString finally source.close())
//          json    <- Task.fromEither(parse(lines))
//        } yield Response.JSON(json)

        
    def get(rc: RoutingContext): Task[Response] =  Task(Response.Empty())

    
  }}
}