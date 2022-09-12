package com.harana.modules.stripe

import com.outr.stripe.connect.FeeRefund
import com.outr.stripe.{Money, QueryConfig, ResponseError, StripeList}
import zio.macros.accessible
import zio.{Has, IO}

@accessible
object StripeApplicationFeeRefunds {

  type StripeApplicationFeeRefunds = Has[StripeApplicationFeeRefunds.Service]

  trait Service {def create(feeId: String,
               amount: Option[Money] = None,
               metadata: Map[String, String] = Map.empty): IO[ResponseError, FeeRefund]

    def byId(feeId: String, refundId: String): IO[ResponseError, FeeRefund]

    def update(feeId: String, refundId: String, metadata: Map[String, String] = Map.empty): IO[ResponseError, FeeRefund]

    def list(feeId: String, config: QueryConfig = QueryConfig.default): IO[ResponseError, StripeList[FeeRefund]]
  }
}