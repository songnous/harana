package com.harana.modules.stripe

import com.harana.modules.core.config.Config
import com.harana.modules.stripe.StripeEvents.Service
import com.harana.modules.core.logger.Logger
import com.harana.modules.core.micrometer.Micrometer
import com.outr.stripe._
import com.outr.stripe.event.Event
import zio.{Has, IO, ZLayer}

object LiveStripeEvents {

  val layer = ZLayer.fromServices { (config: Config.Service,
                                     logger: Logger.Service,
                                     micrometer: Micrometer.Service) => new Service {

    private val client = config.secret("stripe-secret-key").map(key => new Stripe(key).events)

    def byId(eventId: String): IO[ResponseError, Event] =
      for {
        c <- client
        r <- execute(c.byId(eventId))
      } yield r


    def list(created: Option[TimestampFilter] = None,
             `type`: Option[String] = None,
             types: List[String] = Nil,
             config: QueryConfig = QueryConfig.default): IO[ResponseError, StripeList[Event]] =
      for {
        c <- client
        r <- execute(c.list(created, `type`, types, config))
      } yield r
  }}
}
