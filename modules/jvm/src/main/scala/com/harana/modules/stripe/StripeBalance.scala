package com.harana.modules.stripe

import com.outr.stripe.balance.{Balance, BalanceTransaction}
import com.outr.stripe.{QueryConfig, ResponseError, StripeList, TimestampFilter}
import zio.macros.accessible
import zio.{Has, IO}

@accessible
object StripeBalance {

  type StripeBalance = Has[StripeBalance.Service]

  trait Service {
    def get: IO[ResponseError, Balance]

    def byId(id: String, config: QueryConfig = QueryConfig.default): IO[ResponseError, BalanceTransaction]

    def list(availableOn: Option[TimestampFilter] = None,
             created: Option[TimestampFilter] = None,
             currency: Option[String] = None,
             source: Option[String] = None,
             transfer: Option[String] = None,
             `type`: Option[String] = None,
             config: QueryConfig = QueryConfig.default): IO[ResponseError, StripeList[BalanceTransaction]]
  }
}