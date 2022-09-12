package com.harana.sdk.backend.models.flow.actions.examples

import com.harana.sdk.backend.models.flow.actions.SortTransformation
import com.harana.sdk.shared.models.flow.actionobjects.SortColumnParameter

class SortTransformationExample extends AbstractActionExample[SortTransformation] {

  def action: SortTransformation = {
    val op = new SortTransformation
    op.transformer.setColumns(
      Seq(
        SortColumnParameter("city", descending = false),
        SortColumnParameter("price", descending = true)
      )
    )
    op.set(op.transformer.extractParameterMap())
  }

  override val fileNames = Seq("example_city_beds_price")

}
