package com.harana

import com.harana.modules.Layers
import com.harana.modules.calcite.LiveCalcite
import com.harana.modules.core.logger.Logger
import com.harana.modules.core.{Layers => CoreLayers}
import com.harana.modules.shopify.models.ShopifyConnection
import com.harana.modules.shopify.LiveShopify
import zio._

object App extends zio.App {
  val calcite = (CoreLayers.standard) >>> LiveCalcite.layer
  val shopifyPrivate = (CoreLayers.standard ++ Layers.okhttp) >>> LiveShopify.layer

  def startup =
    for {
      connection      <- UIO(ShopifyConnection("", "", ""))
    } yield ()


  def run(args: List[String]): ZIO[zio.ZEnv, Nothing, zio.ExitCode] =
    for {
      exitCode              <- startup.exitCode
    } yield exitCode





  private def log(s: String) =
    Logger.info(s).provideLayer(CoreLayers.logger)











  //      //      q1    <- exec("select A FROM b")
  ////
  ////      q2    <- exec("select A FROM b AS c")
  ////
  ////      q3    <- exec("SELECT Orders.OrderID, Customers.CustomerName, Orders.OrderDate FROM Orders.X " +
  ////                                          "INNER JOIN Customers.Y ON Orders.CustomerID=Customers.CustomerID")
  ////
  ////      q4    <- exec("SELECT country.country_name_eng, SUM(CASE WHEN calls.id IS NOT NULL THEN 1 ELSE 0 END) AS calls" +
  ////                                          " FROM country " +
  ////                                          "LEFT JOIN city ON city.country_id = country.id LEFT JOIN customer ON city.id = customer.city_id " +
  ////                                          "LEFT JOIN calls ON calls.customer_id = customer.id GROUP BY country.id, country.country_name_eng " +
  ////                                          "ORDER BY calls DESC, country.id ASC")
  ////
  ////      q5    <- exec("SELECT * FROM country LEFT JOIN city ON city.country_id = country.id LEFT JOIN customer ON " +
  ////                                          "city.id = customer.city_id LEFT JOIN temp ON temp.customer_id = customer.id")
  ////
  ////      q6    <- exec("SELECT SalesOrderID, LineTotal, (SELECT AVG(LineTotal) FROM Sales.SalesOrderDetail) AS " +
  ////                                          "AverageLineTotal FROM Sales.SalesOrderDetail")
  ////
  ////      q7    <- exec("SELECT SalesOrderID, SalesOrderDetailID, LineTotal, (SELECT AVG(LineTotal) FROM " +
  ////                                          "Sales.SalesOrderDetail WHERE SalesOrderID = SOD.SalesOrderID) AS AverageLineTotal " +
  ////                                          "FROM Sales.SalesOrderDetail SOD")
  ////
  ////      q8    <- exec("SELECT AVG(TOTAL) FROM (SELECT PLAYERNO, SUM(AMOUNT) AS TOTAL FROM PENALTIES GROUP BY PLAYERNO) " +
  ////                                          "AS TOTALS WHERE PLAYERNO IN (SELECT PLAYERNO FROM PLAYERS WHERE TOWN = 'Stratford' OR TOWN = 'Inglewood')")
  ////
  ////      q9    <- exec("select Candidate, Election_year, sum(Total_$), count(*) from combined_party_data where Election_year = 2016 " +
  ////                                          "group by Candidate, Election_year having count(*) > 80 order by count(*) DESC")
  ////
  ////      q10   <- exec("SELECT * FROM A x RIGHT JOIN B y ON y.aId = x.Id")
  ////
  ////      q13   <- exec("select order_date, order_amount from customers join orders on customers.customer_id = orders.customer_id " +
  ////                                           "where customer_id = 3")
  ////
  ////      q14   <- exec("SELECT productID, productName, categoryName, companyName AS supplier FROM products INNER JOIN categories " +
  ////                                           "ON categories.categoryID = products.categoryID INNER JOIN suppliers ON suppliers.supplierID = products.supplierID")
  ////
  ////      q15   <- exec("SELECT job_id,AVG(salary) FROM employees GROUP BY job_id HAVING AVG(salary) < (SELECT MAX(AVG(min_salary)) " +
  ////                                          "FROM jobs WHERE job_id IN (SELECT job_id FROM job_history WHERE department_id BETWEEN 50 AND 100) GROUP BY job_id)")
  ////
  ////      q16    <- exec("select A FROM b AS c ORDER by d")


}
