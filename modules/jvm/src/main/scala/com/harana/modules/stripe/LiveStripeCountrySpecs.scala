package com.harana.modules.stripe

import com.harana.modules.core.config.Config
import com.harana.modules.stripe.StripeCountrySpecs.Service
import com.harana.modules.core.logger.Logger
import com.harana.modules.core.micrometer.Micrometer
import com.outr.stripe.connect.CountrySpec
import com.outr.stripe.{QueryConfig, ResponseError, Stripe, StripeList}
import zio.{Has, IO, ZLayer}

object LiveStripeCountrySpecs {

  val layer = ZLayer.fromServices { (config: Config.Service,
                                     logger: Logger.Service,
                                     micrometer: Micrometer.Service) => new Service {

    private val client = config.secret("stripe-secret-key").map(key => new Stripe(key).countrySpecs)

    def list(config: QueryConfig = QueryConfig.default): IO[ResponseError, StripeList[CountrySpec]] =
      for {
        c <- client
        r <- execute(c.list(config))
      } yield r


    def byId(countryCode: String): IO[ResponseError, CountrySpec] =
      for {
        c <- client
        r <- execute(c.byId(countryCode))
      } yield r
  }}
}