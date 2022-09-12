package com.harana.modules.stripe

import com.harana.modules.core.config.Config
import com.harana.modules.stripe.StripeCustomers.Service
import com.harana.modules.core.logger.Logger
import com.harana.modules.core.micrometer.Micrometer
import com.outr.stripe._
import com.outr.stripe.charge.{Address, Card, Shipping}
import com.outr.stripe.customer.Customer
import zio.{Has, IO, ZLayer}

object LiveStripeCustomers {

  val layer = ZLayer.fromServices { (config: Config.Service,
                                     logger: Logger.Service,
                                     micrometer: Micrometer.Service) => new Service {

    private val client = config.secret("stripe-secret-key").map(key => new Stripe(key).customers)

    def create(address: Option[Address] = None,
               balance: Option[Money] = None,
               coupon: Option[String] = None,
               description: Option[String] = None,
               email: Option[String] = None,
               invoicePrefix: Option[String] = None,
               metadata: Map[String, String] = Map.empty,
               name: Option[String] = None,
               nextInvoiceSequence: Option[Int] = None,
               paymentMethodId: Option[String] = None,
               phone: Option[String] = None,
               promotionCode: Option[String] = None,
               shipping: Option[Shipping] = None,
               source: Option[Card] = None,
               taxExempt: Option[String] = None): IO[ResponseError, Customer] =
      for {
        c <- client
        r <- execute(c.create(address, balance, coupon, description, email, invoicePrefix, metadata, name, nextInvoiceSequence, paymentMethodId, phone, promotionCode, shipping, source, taxExempt))
      } yield r


    def byId(customerId: String): IO[ResponseError, Customer] =
      for {
        c <- client
        r <- execute(c.byId(customerId))
      } yield r


    def update(customerId: String,
               address: Option[Address] = None,
               balance: Option[Money] = None,
               coupon: Option[String] = None,
               defaultSource: Option[String] = None,
               description: Option[String] = None,
               email: Option[String] = None,
               invoicePrefix: Option[String] = None,
               metadata: Map[String, String] = Map.empty,
               name: Option[String] = None,
               nextInvoiceSequence: Option[Int] = None,
               phone: Option[String] = None,
               promotionCode: Option[String] = None,
               shipping: Option[Shipping] = None,
               source: Option[Card] = None,
               taxExempt: Option[String] = None): IO[ResponseError, Customer] =
      for {
        c <- client
        r <- execute(c.update(customerId, address, balance, coupon, defaultSource, description, email, invoicePrefix, metadata, name, nextInvoiceSequence, phone, promotionCode, shipping, source, taxExempt))
      } yield r


    def delete(customerId: String): IO[ResponseError, Deleted] =
      for {
        c <- client
        r <- execute(c.delete(customerId))
      } yield r


    def list(created: Option[TimestampFilter] = None,
             config: QueryConfig = QueryConfig.default,
             email: Option[String] = None): IO[ResponseError, StripeList[Customer]] =
      for {
        c <- client
        r <- execute(c.list(created, config, email))
      } yield r
  }}
}