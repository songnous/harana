package com.harana.modules.stripe

import com.harana.modules.stripe.StripeSubscriptionItems.Service
import com.harana.modules.core.config.Config
import com.harana.modules.core.logger.Logger
import com.harana.modules.core.micrometer.Micrometer
import com.outr.stripe._
import com.outr.stripe.subscription.SubscriptionItem
import zio.{IO, ZLayer}

object LiveStripeSubscriptionItems {

  val layer = ZLayer.fromServices { (config: Config.Service,
                                     logger: Logger.Service,
                                     micrometer: Micrometer.Service) => new Service {

    private val client = config.secret("stripe-secret-key").map(key => new Stripe(key).subscriptionItems)

    def create(subscriptionId: String,
               billingThresholds: Map[String, String] = Map(),
               metadata: Map[String, String] = Map(),
               paymentBehavior: Option[String] = None,
               priceId: Option[String] = None,
               prorationBehavior: Option[String] = None,
               prorationDate: Option[Long] = None,
               quantity: Option[Int] = None,
               taxRates: List[String] = List()): IO[ResponseError, SubscriptionItem] =
      for {
        c <- client
        r <- execute(c.create(subscriptionId, billingThresholds, metadata, paymentBehavior, priceId, prorationBehavior, prorationDate, quantity, taxRates))
      } yield r


    def byId(subscriptionItemId: String): IO[ResponseError, SubscriptionItem] =
      for {
        c <- client
        r <- execute(c.byId(subscriptionItemId))
      } yield r


    def update(subscriptionItemId: String,
               billingThresholds: Map[String, String] = Map(),
               metadata: Map[String, String] = Map(),
               offSession: Option[Boolean] = None,
               paymentBehavior: Option[String] = None,
               priceId: Option[String] = None,
               prorationBehavior: Option[String] = None,
               prorationDate: Option[Long] = None,
               quantity: Option[Int] = None,
               taxRates: List[String] = List()): IO[ResponseError, SubscriptionItem] =
      for {
        c <- client
        r <- execute(c.update(subscriptionItemId, billingThresholds, metadata, offSession, paymentBehavior, priceId, prorationBehavior, prorationDate, quantity, taxRates))
      } yield r


    def delete(subscriptionItemId: String): IO[ResponseError, Deleted] =
      for {
        c <- client
        r <- execute(c.delete(subscriptionItemId))
      } yield r


    def list(subscription: String,
             config: QueryConfig = QueryConfig.default): IO[ResponseError, StripeList[SubscriptionItem]] =
      for {
        c <- client
        r <- execute(c.list(subscription, config))
      } yield r
  }}
}