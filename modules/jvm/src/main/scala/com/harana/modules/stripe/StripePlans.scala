package com.harana.modules.stripe

import com.outr.stripe._
import com.outr.stripe.subscription.Plan
import zio.macros.accessible
import zio.{Has, IO}

@accessible
object StripePlans {

  type StripePlans = Has[StripePlans.Service]

  trait Service {
    def create(planId: String,
               amount: Money,
               currency: String,
               interval: String,
               productId: String,
               intervalCount: Int = 1,
               metadata: Map[String, String] = Map.empty,
               nickname: Option[String],
               trialPeriodDays: Option[Int] = None): IO[ResponseError, Plan]

    def byId(planId: String): IO[ResponseError, Plan]

    def update(planId: String,
               metadata: Map[String, String] = Map.empty,
               name: Option[String] = None,
               productId: Option[String] = None,
               statementDescriptor: Option[String] = None,
               trialPeriodDays: Option[Int] = None): IO[ResponseError, Plan]

    def delete(planId: String): IO[ResponseError, Deleted]

    def list(active: Option[Boolean] = None,
             created: Option[TimestampFilter] = None,
             config: QueryConfig = QueryConfig.default): IO[ResponseError, StripeList[Plan]]
  }
}