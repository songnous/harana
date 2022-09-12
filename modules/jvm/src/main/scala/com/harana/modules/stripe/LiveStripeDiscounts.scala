package com.harana.modules.stripe

import com.harana.modules.core.config.Config
import com.harana.modules.stripe.StripeDiscounts.Service
import com.harana.modules.core.logger.Logger
import com.harana.modules.core.micrometer.Micrometer
import com.outr.stripe.{Deleted, ResponseError, Stripe}
import zio.{Has, IO, ZLayer}

object LiveStripeDiscounts {

  val layer = ZLayer.fromServices { (config: Config.Service,
                                     logger: Logger.Service,
                                     micrometer: Micrometer.Service) => new Service {


    private val client = config.secret("stripe-secret-key").map(key => new Stripe(key).discounts)

    def deleteCustomerDiscount(customerId: String): IO[ResponseError, Deleted] =
      for {
        c <- client
        r <- execute(c.deleteCustomerDiscount(customerId))
      } yield r


    def deleteSubscriptionDiscount(subscriptionId: String): IO[ResponseError, Deleted] =
      for {
        c <- client
        r <- execute(c.deleteSubscriptionDiscount(subscriptionId))
      } yield r
  }}
}