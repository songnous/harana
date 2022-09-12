package com.harana.modules.stripe

import com.harana.modules.core.config.Config
import com.harana.modules.stripe.StripeApplicationFeeRefunds.Service
import com.harana.modules.core.logger.Logger
import com.harana.modules.core.micrometer.Micrometer
import com.outr.stripe._
import com.outr.stripe.connect.FeeRefund
import zio.macros.accessible
import zio.{Has, IO, ZLayer}

object LiveStripeApplicationFeeRefunds {

  val layer = ZLayer.fromServices { (config: Config.Service,
                                     logger: Logger.Service,
                                     micrometer: Micrometer.Service) => new Service {

    private val client = config.secret("stripe-secret-key").map(key => new Stripe(key).applicationFees.refunds)

    def create(feeId: String,
               amount: Option[Money] = None,
               metadata: Map[String, String] = Map.empty): IO[ResponseError, FeeRefund] =
      for {
        c <- client
        r <- execute(c.create(feeId, amount, metadata))
      } yield r


    def byId(feeId: String, refundId: String): IO[ResponseError, FeeRefund] =
      for {
        c <- client
        r <- execute(c.byId(feeId, refundId))
      } yield r


    def update(feeId: String, refundId: String, metadata: Map[String, String] = Map.empty): IO[ResponseError, FeeRefund] =
      for {
        c <- client
        r <- execute(c.update(feeId, refundId, metadata))
      } yield r


    def list(feeId: String, config: QueryConfig = QueryConfig.default): IO[ResponseError, StripeList[FeeRefund]] =
      for {
        c <- client
        r <- execute(c.list(feeId, config))
      } yield r
  }}
}