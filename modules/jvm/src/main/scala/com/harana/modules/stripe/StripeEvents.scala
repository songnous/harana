package com.harana.modules.stripe

import com.outr.stripe.event.Event
import com.outr.stripe.{QueryConfig, ResponseError, StripeList, TimestampFilter}
import zio.macros.accessible
import zio.{Has, IO}

@accessible
object StripeEvents {

  type StripeEvents = Has[StripeEvents.Service]

  trait Service {
    def byId(eventId: String): IO[ResponseError, Event]

    def list(created: Option[TimestampFilter] = None,
             `type`: Option[String] = None,
             types: List[String] = Nil,
             config: QueryConfig = QueryConfig.default): IO[ResponseError, StripeList[Event]]
  }
}