package com.harana.sdk.backend.models.flow.actions.examples

import com.harana.sdk.backend.models.flow.actions.spark.wrappers.estimators.StringIndexer

class StringIndexerExample extends AbstractActionExample[StringIndexer] {

  def action = new StringIndexer().setSingleColumn("city", "city_indexed")

  override def fileNames = Seq("example_city_price")

}
