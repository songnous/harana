package com.harana.modules.shopify

import java.time.{Instant, ZoneId}
import java.time.temporal.{ChronoUnit, TemporalAdjusters}

import com.harana.modules.shopify.Shopify.Service
import com.harana.modules.shopify.models._
import com.harana.modules.core.config.Config
import com.harana.modules.core.logger.Logger
import com.harana.modules.core.micrometer.Micrometer
import com.harana.modules.core.okhttp.OkHttp
import io.circe.Decoder
import io.circe.parser._
import org.apache.commons.lang3.StringUtils
import zio.{Ref, Task, UIO, ZLayer}
import purecsv.safe._

import scala.util.Try
import java.io.File

object LiveShopify {

  val layer = ZLayer.fromServices { (config: Config.Service,
                                     logger: Logger.Service,
                                     micrometer: Micrometer.Service,
                                     okHttp: OkHttp.Service) => new Service {


    def forecastInventory(connection: ShopifyConnection): Task[List[Output]] =
      for {
        products                    <- products(connection, limit = Some(250), status = Some("active")).map(_.items)
        _                           <- logger.info(s"Number of products: ${products.size}")
        productMap                  = products.map(p => p.id -> p).toMap
        variants                    = products.flatMap(p => p.variants)
        variantsMap                 = variants.map(v => v.id -> v).toMap
        _                           <- logger.info(s"Number of variants: ${variantsMap.size}")

        outputs                     = variants.map(v => Output(productMap(v.productId).title, v.title, v.sku.getOrElse(""), v.id, v.option1.getOrElse(""), v.option2.getOrElse(""), v.option3.getOrElse(""), "0", "0", "0", "0", "0"))
        orders                      <- all(connection, orders(connection, limit = Some(250), status = Some("any")))
        _                           <- logger.info(s"Number of orders: ${orders.size}")

        ordersByDate                = orders.groupBy(o => o.createdAt.atZone(ZoneId.systemDefault()).`with`(TemporalAdjusters.firstDayOfMonth()).truncatedTo(ChronoUnit.DAYS))
        lineItemsByDate             = ordersByDate.mapValues(orders => orders.flatMap(_.lineItems.map(li => (lineItemTitle(li), li.quantity))))
        lineItemsByVariantIdMap     = orders.flatMap(_.lineItems.map(li => li.variantId -> li)).toMap
        groupedLineItemsByDate      = lineItemsByDate.mapValues(lineItems => lineItems.groupBy(_._1).mapValues(_.map(_._2).sum).toList.sortBy(_._1))

        groupedLineItemsByDateMap   = groupedLineItemsByDate.mapValues(sumByKeys).mapValues(_.toMap)
        sortedDates                 = groupedLineItemsByDate.keys.toList.sortBy(_.toString).take(3)
        _                           <- logger.info(s"Dates to output: ${sortedDates.map(_.toString)}")

        middleOutputs = outputs.map { o =>
          if (lineItemsByVariantIdMap.contains(o.variantId)) {
            val lineItem = lineItemsByVariantIdMap(o.variantId)

            val month1 = Try(groupedLineItemsByDateMap(sortedDates.head)(lineItemTitle(lineItem))).toOption.getOrElse(0L)
            val month2 = Try(groupedLineItemsByDateMap(sortedDates(1))(lineItemTitle(lineItem))).toOption.getOrElse(0L)
            val month3 = Try(groupedLineItemsByDateMap(sortedDates(2))(lineItemTitle(lineItem))).toOption.getOrElse(0L)
            val total = month1 + month2 + month3
            o.copy(month1Sales = month1.toString, month2Sales = month2.toString, month3Sales = month3.toString, totalSales = total.toString)
          } else {
            o.copy(month1Sales = "-", month2Sales = "-", month3Sales = "-", totalSales = "-")
          }
        }

        location <- locations(connection).map(_.items.head)
        _ <- logger.info(s"Found location with id: ${location.id}")

        inventoryLevels <- all(connection, inventoryLevels(connection, limit = Some(250), locationIds = List(location.id)))
        inventoryLevelsMap = inventoryLevels.map(il => il.inventoryItemId -> il.available).toMap
        finalOutputs = middleOutputs.map(o => o.copy(inventoryLevel = Try(inventoryLevelsMap(variantsMap(o.variantId).inventoryItemId).toString).toOption.getOrElse("-")))

        _ = finalOutputs.writeCSVToFile(new File("/tmp/output.csv"))

      } yield finalOutputs


    private def sumByKeys[A](tuples: List[(A, Long)]) : List[(A, Long)] = {
      tuples.groupBy(_._1).mapValues(_.map(_._2).sum).toList
    }


    private def lineItemTitle(li: LineItem) =
      s"${li.productId}-${li.variantId}"


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
               fields: List[String] = List()): Task[Page[Order]] =
      getList[Order](connection, s"orders", Map("ids" -> ids.mkString(","), "limit" -> limit.getOrElse(50).toString, "status" -> status.getOrElse("")))


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
                 fields: List[String] = List()): Task[Page[models.Product]] =
      getList[models.Product](connection, s"products", Map(
        "ids" -> ids.mkString(","),
        "limit" -> limit.getOrElse(50).toString,
        "status" -> status.getOrElse(""))
      )


    def inventoryLevels(connection: ShopifyConnection,
                        inventoryItemIds: List[Long] = List(),
                        locationIds: List[Long] = List(),
                        limit: Option[Int] = None,
                        updatedAtMin: Option[String] = None): Task[Page[InventoryLevel]] =
      getList[InventoryLevel](connection, s"inventory_levels", Map(
        "limit" -> limit.getOrElse(50).toString,
        "location_ids" -> locationIds.map(_.toString).mkString(",")))


    def locations(connection: ShopifyConnection): Task[Page[Location]] =
      getList[Location](connection, s"locations", Map())


    def customer(connection: ShopifyConnection,
                 id: String,
                 fields: List[String] = List()): Task[Customer] =
      get[Customer](connection, s"customers/$id", Map())


    def product(connection: ShopifyConnection,
                id: String,
                fields: List[String] = List()): Task[models.Product] =
      get[models.Product](connection, s"products/$id", Map())


    def productVariants(connection: ShopifyConnection,
                        productId: Long,
                        limit: Option[Int] = None,
                        presentmentCurrencies: List[String] = List(),
                        sinceId: Option[String] = None,
                        fields: List[String] = List()): Task[Page[ProductVariant]] =
      getList[ProductVariant](connection, s"products/$productId/variants", Map(
        "fields" -> fields.mkString(","),
        "limit" -> limit.getOrElse(50).toString,
        "presentment_currencies" -> presentmentCurrencies.mkString(","),
        "since_id" -> sinceId.getOrElse("")))


    def previousPage[T](connection: ShopifyConnection, page: Page[T])(implicit d: Decoder[T]): Task[Option[Page[T]]] =
      Task.foreach(page.previousUrl)(url => getUrlList[T](connection, url, Map()))


    def nextPage[T](connection: ShopifyConnection, page: Page[T])(implicit d: Decoder[T]): Task[Option[Page[T]]] =
      Task.foreach(page.nextUrl)(url => getUrlList[T](connection, url, Map()))


    def all[T](connection: ShopifyConnection, fn: => Task[Page[T]])(implicit d: Decoder[T]): Task[List[T]] =
      for {
        itemsRef            <- Ref.make[List[T]](List())
        initialPage         <- fn
        currentPageRef      <- Ref.make[Option[Page[T]]](Some(initialPage))
        _                   <- (for {
                                  currentPage       <- currentPageRef.get
                                  existingItems     <- itemsRef.get
                                  _                 <- itemsRef.set (existingItems ++ currentPage.get.items)
                                  nextPage          <- Task.foreach(currentPage)(nextPage[T](connection, _)).map(_.flatten)
                                  _                 <- currentPageRef.set(nextPage)
                                } yield ()).repeatWhileM { _ => currentPageRef.get.map(_.isDefined) }
        items               <- itemsRef.get
      } yield items


    private def get[T](connection: ShopifyConnection, endpoint: String, parameters: Map[String, String])(implicit d: Decoder[T]): Task[T] =
      for {
        url           <- UIO(s"https://${connection.subdomain}.myshopify.com/admin/api/2020-07/$endpoint.json")
        response      <- okHttp.get(url, params = parameters.map{ case (k, v) => k -> List(v) }, credentials = Some((connection.apiKey, connection.password))).mapBoth(e => new Exception(e.toString), _.body().string())
        obj           <- Task.fromEither(decode[T](response))
      } yield obj


    private def getList[T](connection: ShopifyConnection, endpoint: String, parameters: Map[String, String])(implicit d: Decoder[T]): Task[Page[T]] =
      for {
        url           <- UIO(s"https://${connection.subdomain}.myshopify.com/admin/api/2020-07/$endpoint.json")
        page          <- getUrlList[T](connection, url, parameters)
      } yield page


    private def getUrlList[T](connection: ShopifyConnection, url: String, parameters: Map[String, String])(implicit d: Decoder[T]): Task[Page[T]] =
      for {
        response      <- okHttp.get(url, params = parameters.map{ case (k, v) => k -> List(v) }, credentials = Some((connection.apiKey, connection.password))).mapError(e => new Exception(e.toString))

        rel           =  Option(response.header("link"))
        relUrl        =  rel.map(r => r.substring(1, r.indexOf(">")))
        relType       =  rel.map(r => r.substring(r.indexOf("rel=")+5, r.length-1))

        cursor        <- Task(parse(response.body().string).right.get.hcursor)
        root          <- Task(cursor.keys.get.head)
        json          <- Task(cursor.downField(root).focus.get)
        items         <- Task.fromEither(json.as[List[T]]).onError(e => logger.error(e.prettyPrint))

        page          =  (rel, relType) match {
                            case (None, _)                                    => Page(None, None, items)
                            case (Some(_), Some(rt)) if (rt == "previous")    => Page(relUrl, None, items)
                            case (Some(_), Some(rt)) if (rt == "next")        => Page(None, relUrl, items)
                          }
      } yield page
  }}
}
