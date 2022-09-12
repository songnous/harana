package com.harana.modules.stripe

import com.harana.modules.stripe.StripePrices.Service
import com.harana.modules.core.config.Config
import com.harana.modules.core.logger.Logger
import com.harana.modules.core.micrometer.Micrometer
import com.outr.stripe._
import com.outr.stripe.price.{Price, Recurring, Tier, TransformQuantity}
import zio.{IO, ZLayer}

object LiveStripePrices {

  val layer = ZLayer.fromServices { (config: Config.Service,
                                     logger: Logger.Service,
                                     micrometer: Micrometer.Service) => new Service {

    private val client = config.secret("stripe-secret-key").map(key => new Stripe(key).prices)

    def create(currency: String,
               active: Option[Boolean] = None,
               billingScheme: Option[String] = None,
               lookupKey: Option[String] = None,
               metadata: Map[String, String] = Map.empty,
               nickname: Option[String] = None,
               recurring: Option[Recurring] = None,
               tiers: List[Tier] = List(),
               tiersMode: Option[String] = None,
               transferLookupKey: Option[Boolean] = None,
               transformQuantity: Option[TransformQuantity] = None,
               unitAmount: Option[Int] = None,
               unitAmountDecimal: Option[BigDecimal] = None): IO[ResponseError, Price] =
      for {
        c <- client
        r <- execute(c.create(currency, active, billingScheme, lookupKey, metadata, nickname, recurring, tiers, tiersMode, transferLookupKey, transformQuantity, unitAmount, unitAmountDecimal))
      } yield r


    def byId(priceId: String): IO[ResponseError, Price] =
      for {
        c <- client
        r <- execute(c.byId(priceId))
      } yield r


    def update(priceId: String,
               active: Option[Boolean] = None,
               lookupKey: Option[String] = None,
               metadata: Map[String, String] = Map.empty,
               nickname: Option[String] = None,
               transferLookupKey: Option[Boolean] = None): IO[ResponseError, Price] =
      for {
        c <- client
        r <- execute(c.update(priceId, active, lookupKey, metadata, nickname, transferLookupKey))
      } yield r


    def delete(priceId: String): IO[ResponseError, Deleted] =
      for {
        c <- client
        r <- execute(c.delete(priceId))
      } yield r


    def list(active: Option[Boolean] = None,
             currency: Option[String] = None,
             created: Option[TimestampFilter] = None,
             config: QueryConfig = QueryConfig.default,
             endingBefore: Option[String] = None,
             limit: Option[Int] = None,
             productId: Option[String] = None,
             `type`: Option[String] = None): IO[ResponseError, StripeList[Price]] =
      for {
        c <- client
        r <- execute(c.list(active, currency, created, config, endingBefore, limit, productId, `type`))
      } yield r
  }}
}