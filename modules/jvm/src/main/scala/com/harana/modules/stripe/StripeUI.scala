package com.harana.modules.stripe

import zio.macros.accessible
import zio.{Has, Task}

@accessible
object StripeUI {

  type StripeUI = Has[StripeUI.Service]

  trait Service {
    def billingPortalUrl(customerId: String, returnUrl: String): Task[String]

    def createCheckoutSession(customerId: String, priceId: String, successUrl: String, cancelUrl: String): Task[String]
  }
}