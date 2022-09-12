package com.harana.sdk.backend.models.flow.actions.examples

import com.harana.sdk.backend.models.flow.actions.Split
import com.harana.sdk.shared.models.flow.actions.SplitModeChoice

class SplitExample extends AbstractActionExample[Split] {

  def action = Split().setSplitMode(SplitModeChoice.Random().setSeed(0).setSplitRatio(0.2))

  override def fileNames = Seq("example_city_beds_price")

}
