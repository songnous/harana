package com.harana.sdk.backend.models.flow.actiontypes.examples

import com.harana.sdk.backend.models.flow.actiontypes.SqlCombine

class SqlCombineExample extends AbstractActionExample[SqlCombine] {

  def action: SqlCombine = {
    new SqlCombine()
      .setLeftTableName("beds")
      .setRightTableName("prices")
      .setSqlCombineExpression("""
                                 |SELECT DISTINCT beds.city, beds.beds
                                 |FROM beds
                                 |JOIN prices ON beds.city = prices.city
                                 |AND prices.price < 120000 * beds.beds
        """.stripMargin)
  }

  override def fileNames = Seq("example_city_beds", "example_city_price")

}
