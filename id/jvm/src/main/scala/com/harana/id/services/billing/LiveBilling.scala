package com.harana.id.services.billing

import com.harana.id.jwt.modules.jwt.JWT
import com.harana.id.services.auth.Auth
import com.harana.id.services.billing.Billing.Service
import com.harana.modules.core.config.Config
import com.harana.modules.core.logger.Logger
import com.harana.modules.core.micrometer.Micrometer
import com.harana.modules.mongo.Mongo
import com.harana.modules.stripe.{StripeCustomers, StripeUI}
import com.harana.modules.vertx.Vertx
import com.harana.modules.vertx.models.Response
import com.harana.sdk.shared.models.common.{Event, User}
import com.harana.sdk.shared.models.jwt.DesignerClaims
import com.stripe.model.Subscription
import com.stripe.net.Webhook
import io.circe.syntax._
import io.vertx.core.http.HttpHeaders
import io.vertx.ext.web.RoutingContext
import java.time.Instant
import zio.{Task, ZLayer}

object LiveBilling {
  val layer = ZLayer.fromServices { (auth: Auth.Service,
                                     config: Config.Service,
                                     jwt: JWT.Service,
                                     logger: Logger.Service,
                                     micrometer: Micrometer.Service,
                                     mongo: Mongo.Service,
                                     stripeCustomers: StripeCustomers.Service,
                                     stripeUI: StripeUI.Service,
                                     vertx: Vertx.Service) => new Service {

    def checkout(rc: RoutingContext): Task[Response] =
      for {
        domain                  <- config.env("harana_domain")
        refererUrl              <- Task(rc.request.getHeader(HttpHeaders.REFERER))
        successUrl              =  s"https://id.$domain/billing/checkout/success/{CHECKOUT_SESSION_ID}"
        jwtJson                 <- Task(rc.request.getHeader("jwt"))
        claims                  <- jwt.claims[DesignerClaims](jwtJson)
        key                     <- config.secret("stripe-publishable-key")
        priceId                 <- config.string("stripe.standardPriceId")
        sessionId               <- stripeUI.createCheckoutSession(claims.billing.subscriptionCustomerId.get, priceId, successUrl, refererUrl)
        json                    =  Map("publishableKey" -> key, "sessionId" -> sessionId).asJson
        _                       <- vertx.sendMessage(claims.userId, "analytics", "subscription.checkout")
        response                =  Response.JSON(json)
      } yield response


    def checkoutSuccess(rc: RoutingContext): Task[Response] =
      for {
        claims                  <- jwt.claims[DesignerClaims](rc)
        user                    <- mongo.findOne[User]("Users", Map("id" -> claims.userId)).map(_.get)
        response                <- auth.redirectToApp(user, None)
      } yield response


    def portal(rc: RoutingContext): Task[Response] =
      for {
        returnUrl               <- Task(rc.request.getHeader(HttpHeaders.REFERER))
        claims                  <- jwt.claims[DesignerClaims](rc)
        url                     <- stripeUI.billingPortalUrl(claims.billing.subscriptionCustomerId.get, returnUrl)
        response                =  Response.Redirect(url)
      } yield response


    def subscriptionWebhook(rc: RoutingContext): Task[Response] =
      for {
        secretKey               <- config.secret("stripe-webhook-key")
        signature               <- Task(rc.request.getHeader("Stripe-Signature"))
        payload                 <- Task(rc.body().asString)
        event                   <- Task(Webhook.constructEvent(payload, signature, secretKey))
        subscription            <- Task(event.getDataObjectDeserializer.deserializeUnsafe().asInstanceOf[Subscription])

        _                       <- logger.info(s"Webhook received: ${event.getType} for customer: ${subscription.getCustomer}")

        userId                  <- stripeCustomers.byId(subscription.getCustomer).mapBoth(e => new Exception(e.text), _.metadata.get("haranaId")).map(_.get)

        user                    <- mongo.findOne[User]("Users", Map("id" -> userId)).map(_.get)
        _                       <- Task.when(event.getType.equals("customer.subscription.created"))(
                                      mongo.replace[User]("Users", userId, user.copy(billing = user.billing.copy(subscriptionCustomerId = Some(subscription.getCustomer)), updated = Instant.now), true) *>
                                      mongo.insert[Event]("Events", Event("subscribe", Map(), userId))
                                    )

        user                    <- mongo.findOne[User]("Users", Map("id" -> userId)).map(_.get)
        _                       <- Task.when(event.getType.equals("customer.subscription.updated"))(
                                      if (subscription.getCancelAtPeriodEnd) {
                                        mongo.replace[User]("Users", userId, user.copy(billing = user.billing.copy(subscriptionEnded = Some(Instant.now)), updated = Instant.now), true) *>
                                        mongo.insert[Event]("Events", Event("unsubscribe", Map(), userId))
                                      }else{
                                        mongo.replace[User]("Users", userId, user.copy(billing = user.billing.copy(subscriptionEnded = None), updated = Instant.now), true) *>
                                        mongo.insert[Event]("Events", Event("subscribe", Map(), userId))
                                      }
                                    )

        response                =  Response.Empty()
      } yield response

  }}
}