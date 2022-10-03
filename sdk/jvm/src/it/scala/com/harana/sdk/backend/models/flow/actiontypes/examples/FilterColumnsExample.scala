package com.harana.sdk.backend.models.flow.actiontypes.examples

import com.harana.sdk.backend.models.flow.actiontypes.FilterColumns

class FilterColumnsExample extends AbstractActionExample[FilterColumns] {

  def action: FilterColumns = {
    val op = new FilterColumns()
    op.transformer.setSelectedColumns(Seq("city", "price"))
    op.set(op.transformer.extractParameterMap())
  }

  override def fileNames = Seq("example_city_beds_price")

}
