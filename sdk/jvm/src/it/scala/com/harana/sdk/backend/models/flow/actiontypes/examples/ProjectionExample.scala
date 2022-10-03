package com.harana.sdk.backend.models.flow.actiontypes.examples

import com.harana.sdk.backend.models.flow.actionobjects.Projector.ColumnProjection
import com.harana.sdk.backend.models.flow.actionobjects.Projector.RenameColumnChoice.Yes
import com.harana.sdk.backend.models.flow.actiontypes.Projection
import com.harana.sdk.shared.models.flow.parameters.selections.NameSingleColumnSelection

class ProjectionExample extends AbstractActionExample[Projection] {

  def action: Projection = {
    val op = new Projection()
    op.transformer.setProjectionColumns(
      Seq(
        ColumnProjection().setOriginalColumn(NameSingleColumnSelection("price")),
        ColumnProjection().setOriginalColumn(NameSingleColumnSelection("city")),
        ColumnProjection().setOriginalColumn(NameSingleColumnSelection("city")).setRenameColumn(Yes().setColumnName("location"))
      )
    )
    op.set(op.transformer.extractParameterMap())
  }

  override def fileNames = Seq("example_city_beds_price")

}
