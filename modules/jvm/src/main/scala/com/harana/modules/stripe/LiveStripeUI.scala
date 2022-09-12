package com.harana.modules.stripe

import com.harana.modules.core.okhttp.OkHttp
import com.harana.modules.stripe.StripeUI.Service
import com.harana.modules.vertx.models.Response
import com.harana.modules.core.config.Config
import com.harana.modules.core.logger.Logger
import com.harana.modules.core.micrometer.Micrometer
import zio.{Task, ZLayer}

object LiveStripeUI {

  val layer = ZLayer.fromServices { (config: Config.Service,
                                     logger: Logger.Service,
                                     micrometer: Micrometer.Service,
                                     okHttp: OkHttp.Service) => new Service {

    def billingPortalUrl(customerId: String, returnUrl: String): Task[String] =
      for {
        apiKey      <- config.secret("stripe-secret-key")
        formBody    =  Map(
                          "customer" -> customerId,
                          "return_url" -> returnUrl
                        )
        response    <- okHttp.postFormAsJson("https://api.stripe.com/v1/billing_portal/sessions", formBody, credentials = Some((apiKey, ""))).mapError(e => new Exception(e.toString))
        url         <- Task.fromTry(response.hcursor.downField("url").as[String].toTry)
      } yield url


    def createCheckoutSession(customerId: String, priceId: String, successUrl: String, cancelUrl: String): Task[String] =
      for {
        apiKey      <- config.secret("stripe-secret-key")
        formBody    =  Map(
                        "cancel_url" -> cancelUrl,
                        "customer" -> customerId,
                        "line_items[][price]" -> priceId,
                        "line_items[][quantity]" -> "1",
                        "mode" -> "subscription",
                        "payment_method_types[]" -> "card",
                        "success_url" -> successUrl
                      )
        response    <- okHttp.postFormAsJson("https://api.stripe.com/v1/checkout/sessions", formBody, credentials = Some((apiKey, ""))).mapError(e => new Exception(e.toString))
        id          <- Task.fromTry(response.hcursor.downField("id").as[String].toTry)
      } yield id
  }}
}
