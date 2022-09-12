package com.harana.modules.stripe

import com.harana.modules.core.config.Config
import com.harana.modules.stripe.StripeCoupons.Service
import com.harana.modules.core.logger.Logger
import com.harana.modules.core.micrometer.Micrometer
import com.outr.stripe._
import com.outr.stripe.subscription.Coupon
import zio.{Has, IO, ZLayer}

object LiveStripeCoupons {

  val layer = ZLayer.fromServices { (config: Config.Service,
                                     logger: Logger.Service,
                                     micrometer: Micrometer.Service) => new Service {

    private val client = config.secret("stripe-secret-key").map(key => new Stripe(key).coupons)

    def create(couponId: String,
               duration: String,
               amountOff: Option[Money] = None,
               currency: Option[String] = None,
               durationInMonths: Option[Int] = None,
               maxRedemptions: Option[Int] = None,
               metadata: Map[String, String] = Map.empty,
               percentOff: Option[Int] = None,
               redeemBy: Option[Long] = None): IO[ResponseError, Coupon] =
      for {
        c <- client
        r <- execute(c.create(couponId, duration, amountOff, currency, durationInMonths, maxRedemptions, metadata, percentOff, redeemBy))
      } yield r


    def byId(couponId: String): IO[ResponseError, Coupon] =
      for {
        c <- client
        r <- execute(c.byId(couponId))
      } yield r


    def update(couponId: String, metadata: Map[String, String]): IO[ResponseError, Coupon] =
      for {
        c <- client
        r <- execute(c.update(couponId, metadata))
      } yield r


    def delete(couponId: String): IO[ResponseError, Deleted] =
      for {
        c <- client
        r <- execute(c.delete(couponId))
      } yield r


    def list(created: Option[TimestampFilter] = None,
             config: QueryConfig = QueryConfig.default): IO[ResponseError, StripeList[Coupon]] =
      for {
        c <- client
        r <- execute(c.list(created, config))
      } yield r
  }}
}