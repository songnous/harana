package com.harana.modules.stripe

import com.outr.stripe.{Deleted, ResponseError}
import zio.macros.accessible
import zio.{Has, IO}

@accessible
object StripeDiscounts {

  type StripeDiscount = Has[StripeDiscounts.Service]

  trait Service {
    def deleteCustomerDiscount(customerId: String): IO[ResponseError, Deleted]

    def deleteSubscriptionDiscount(subscriptionId: String): IO[ResponseError, Deleted]
  }
}