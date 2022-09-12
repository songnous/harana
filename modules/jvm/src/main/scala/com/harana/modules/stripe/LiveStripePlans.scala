package com.harana.modules.stripe

import com.harana.modules.core.config.Config
import com.harana.modules.stripe.StripePlans.Service
import com.harana.modules.core.logger.Logger
import com.harana.modules.core.micrometer.Micrometer
import com.outr.stripe._
import com.outr.stripe.subscription.Plan
import zio.{Has, IO, ZLayer}

object LiveStripePlans {

  val layer = ZLayer.fromServices { (config: Config.Service,
                                     logger: Logger.Service,
                                     micrometer: Micrometer.Service) => new Service {

    private val client = config.secret("stripe-secret-key").map(key => new Stripe(key).plans)

    def create(planId: String,
               amount: Money,
               currency: String,
               interval: String,
               productId: String,
               intervalCount: Int = 1,
               metadata: Map[String, String] = Map.empty,
               nickname: Option[String],
               trialPeriodDays: Option[Int] = None): IO[ResponseError, Plan] =
      for {
        c <- client
        r <- execute(c.create(planId, amount, currency, interval, productId, intervalCount, metadata, nickname, trialPeriodDays))
      } yield r


    def byId(planId: String): IO[ResponseError, Plan] =
      for {
        c <- client
        r <- execute(c.byId(planId))
      } yield r


    def update(planId: String,
               metadata: Map[String, String] = Map.empty,
               name: Option[String] = None,
               productId: Option[String] = None,
               statementDescriptor: Option[String] = None,
               trialPeriodDays: Option[Int] = None): IO[ResponseError, Plan] =
      for {
        c <- client
        r <- execute(c.update(planId, metadata, name, productId, statementDescriptor, trialPeriodDays))
      } yield r


    def delete(planId: String): IO[ResponseError, Deleted] =
      for {
        c <- client
        r <- execute(c.delete(planId))
      } yield r


    def list(active: Option[Boolean] = None,
             created: Option[TimestampFilter] = None,
             config: QueryConfig = QueryConfig.default): IO[ResponseError, StripeList[Plan]] =
      for {
        c <- client
        r <- execute(c.list(active, created, config))
      } yield r
  }}
}