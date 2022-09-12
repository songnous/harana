package com.harana.modules.stripe

import com.outr.stripe.connect.ApplicationFee
import com.outr.stripe.{QueryConfig, ResponseError, StripeList, TimestampFilter}
import zio.macros.accessible
import zio.{Has, IO}

@accessible
object StripeApplicationFees {

  type StripeApplicationFees = Has[StripeApplicationFees.Service]

  trait Service {
    def byId(feeId: String): IO[ResponseError, ApplicationFee]

    def list(charge: Option[String] = None,
             created: Option[TimestampFilter] = None,
             config: QueryConfig = QueryConfig.default): IO[ResponseError, StripeList[ApplicationFee]]
  }
}