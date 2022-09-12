package com.harana.modules.shopify

import com.harana.modules.shopify.models.{Customer, InventoryLevel, Location, Order, Page, ProductVariant, ShopifyConnection}
import io.circe.Decoder
import zio.{Has, Task}
import zio.macros.accessible

@accessible
object Shopify {
  type Shopify = Has[Shopify.Service]

  trait Service {

    def all[T](connection: ShopifyConnection, fn: => Task[Page[T]])(implicit d: Decoder[T]): Task[List[T]]

    def previousPage[T](connection: ShopifyConnection, page: Page[T])(implicit d: Decoder[T]): Task[Option[Page[T]]]

    def nextPage[T](connection: ShopifyConnection, page: Page[T])(implicit d: Decoder[T]): Task[Option[Page[T]]]

    def forecastInventory(connection: ShopifyConnection): Task[List[Output]]

    def orders(connection: ShopifyConnection,
               ids: List[String] = List(),
               limit: Option[Int] = None,
               sinceId: Option[String] = None,
               createdAtMin: Option[String] = None,
               createdAtMax: Option[String] = None,
               updatedAtMin: Option[String] = None,
               updatedAtMax: Option[String] = None,
               processedAtMin: Option[String] = None,
               processedAtMax: Option[String] = None,
               attributionAppId: Option[String] = None,
               status: Option[String] = None,
               financialStatus: Option[String] = None,
               fulfillment_status: Option[String] = None,
               fields: List[String] = List()): Task[Page[Order]]

    def products(connection: ShopifyConnection,
                 ids: List[String] = List(),
                 limit: Option[Int] = None,
                 sinceId: Option[String] = None,
                 title: Option[String] = None,
                 vendor: Option[String] = None,
                 handle: Option[String] = None,
                 productType: Option[String] = None,
                 status: Option[String] = None,
                 collectionId: Option[String] = None,
                 createdAtMin: Option[String] = None,
                 createdAtMax: Option[String] = None,
                 updatedAtMin: Option[String] = None,
                 updatedAtMax: Option[String] = None,
                 processedAtMin: Option[String] = None,
                 processedAtMax: Option[String] = None,
                 publishedStatus: Option[String] = None,
                 fields: List[String] = List()): Task[Page[models.Product]]


    def inventoryLevels(connection: ShopifyConnection,
                        inventoryItemIds: List[Long] = List(),
                        locationIds: List[Long] = List(),
                        limit: Option[Int] = None,
                        updatedAtMin: Option[String] = None): Task[Page[InventoryLevel]]


    def customer(connection: ShopifyConnection,
                 id: String,
                 fields: List[String] = List()): Task[Customer]


    def locations(connection: ShopifyConnection): Task[Page[Location]]


    def product(connection: ShopifyConnection,
                id: String,
                fields: List[String] = List()): Task[models.Product]

    def productVariants(connection: ShopifyConnection,
                        productId: Long,
                        limit: Option[Int] = None,
                        presentmentCurrencies: List[String] = List(),
                        sinceId: Option[String] = None,
                        fields: List[String] = List()): Task[Page[ProductVariant]]
  }
}