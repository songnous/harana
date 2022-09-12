package com.harana.modules.stripe

import com.outr.stripe.ResponseError
import com.outr.stripe.charge.{BankAccount, Card, PII}
import com.outr.stripe.token.Token
import zio.macros.accessible
import zio.{Has, IO}

@accessible
object StripeTokens {

  type StripeTokens = Has[StripeTokens.Service]

  trait Service {
    def create(card: Option[Card] = None,
               bankAccount: Option[BankAccount] = None,
               pii: Option[PII] = None,
               customerId: Option[String] = None): IO[ResponseError, Token]

    def byId(tokenId: String): IO[ResponseError, Token]
  }
}