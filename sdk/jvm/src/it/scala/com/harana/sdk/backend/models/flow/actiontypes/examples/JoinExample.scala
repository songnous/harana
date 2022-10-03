package com.harana.sdk.backend.models.flow.actiontypes.examples

import com.harana.sdk.shared.models.designer.flow.actions.Join.ColumnPair
import com.harana.sdk.shared.models.designer.flow.actions.Join
import com.harana.sdk.shared.models.flow.parameters.selections.{NameColumnSelection, NameSingleColumnSelection}

class JoinExample extends AbstractActionExample[Join] {

  def action: Join = {
    new Join()
      .setLeftPrefix("left_")
      .setRightPrefix("right_")
      .setJoinColumns(
        Seq(
          ColumnPair()
            .setLeftColumn(NameSingleColumnSelection("city"))
            .setRightColumn(NameSingleColumnSelection("city"))
        )
      )
  }

  override def fileNames = Seq("example_city_price", "example_city_beds")

}
