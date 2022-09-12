package com.harana.modules.stripe

import com.harana.modules.core.config.Config
import com.harana.modules.stripe.StripeBalance.Service
import com.harana.modules.core.logger.Logger
import com.harana.modules.core.micrometer.Micrometer
import com.outr.stripe._
import com.outr.stripe.balance.{Balance, BalanceTransaction}
import zio.{Has, IO, ZLayer}

object LiveStripeBalance {

  val layer = ZLayer.fromServices { (config: Config.Service,
                                     logger: Logger.Service,
                                     micrometer: Micrometer.Service) => new Service {

    private val client = config.secret("stripe-secret-key").map(key => new Stripe(key).balance)

    def get: IO[ResponseError, Balance] =
      for {
        c <- client
        r <- execute(c.apply())
      } yield r


    def byId(id: String, config: QueryConfig = QueryConfig.default): IO[ResponseError, BalanceTransaction] =
      for {
        c <- client
        r <- execute(c.byId(id, config))
      } yield r


    def list(availableOn: Option[TimestampFilter] = None,
             created: Option[TimestampFilter] = None,
             currency: Option[String] = None,
             source: Option[String] = None,
             transfer: Option[String] = None,
             `type`: Option[String] = None,
             config: QueryConfig = QueryConfig.default): IO[ResponseError, StripeList[BalanceTransaction]] =
      for {
        c <- client
        r <- execute(c.list(availableOn, created, currency, source, transfer, `type`, config))
      } yield r
  }}
}