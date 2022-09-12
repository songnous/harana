package com.harana.modules.stripe

import com.outr.stripe.refund.Refund
import com.outr.stripe.{Money, QueryConfig, ResponseError, StripeList}
import zio.macros.accessible
import zio.{Has, IO}

@accessible
object StripeRefunds {

  type StripeRefunds = Has[StripeRefunds.Service]

  trait Service {
    def create(chargeId: String,
               amount: Option[Money] = None,
               metadata: Map[String, String] = Map.empty,
               reason: Option[String] = None,
               refundApplicationFee: Boolean = false,
               reverseTransfer: Boolean = false): IO[ResponseError, Refund]

    def byId(refundId: String): IO[ResponseError, Refund]

    def update(refundId: String, metadata: Map[String, String] = Map.empty): IO[ResponseError, Refund]

    def list(chargeId: Option[String] = None,
             config: QueryConfig = QueryConfig.default): IO[ResponseError, StripeList[Refund]]
  }
}