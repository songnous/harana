package com.harana.id.services.billing

import com.harana.modules.vertx.models.Response
import io.vertx.ext.web.RoutingContext
import zio.macros.accessible
import zio.{Has, Task}

@accessible
object Billing {
  type Billing = Has[Billing.Service]

  trait Service {
    def checkout(rc: RoutingContext): Task[Response]

    def checkoutSuccess(rc: RoutingContext): Task[Response]

    def portal(rc: RoutingContext): Task[Response]

    def subscriptionWebhook(rc: RoutingContext): Task[Response]
  }
}