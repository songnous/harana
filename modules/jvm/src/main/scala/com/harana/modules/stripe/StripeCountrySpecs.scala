package com.harana.modules.stripe

import com.outr.stripe.connect.CountrySpec
import com.outr.stripe.{QueryConfig, ResponseError, StripeList}
import zio.macros.accessible
import zio.{Has, IO}

@accessible
object StripeCountrySpecs {

  type StripeCountrySpecs = Has[StripeCountrySpecs.Service]

  trait Service {
    def list(config: QueryConfig = QueryConfig.default): IO[ResponseError, StripeList[CountrySpec]]

    def byId(countryCode: String): IO[ResponseError, CountrySpec]
  }
}