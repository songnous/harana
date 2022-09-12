package com.harana.modules.stripe

import com.harana.modules.core.config.Config
import com.harana.modules.stripe.StripeTokens.Service
import com.harana.modules.core.logger.Logger
import com.harana.modules.core.micrometer.Micrometer
import com.outr.stripe.charge.{BankAccount, Card, PII}
import com.outr.stripe.token.Token
import com.outr.stripe.{ResponseError, Stripe}
import zio.{Has, IO, ZLayer}

object LiveStripeTokens {

  val layer = ZLayer.fromServices { (config: Config.Service,
                                     logger: Logger.Service,
                                     micrometer: Micrometer.Service) => new Service {

    private val client = config.secret("stripe-secret-key").map(key => new Stripe(key).tokens)

    def create(card: Option[Card] = None,
               bankAccount: Option[BankAccount] = None,
               pii: Option[PII] = None,
               customerId: Option[String] = None): IO[ResponseError, Token] =
      for {
					c <- client
          r <- execute(c.create(card, bankAccount, pii, customerId))
      } yield r


    def byId(tokenId: String): IO[ResponseError, Token] =
      for {
        c <- client
        r <- execute(c.byId(tokenId))
      } yield r
  }}
}