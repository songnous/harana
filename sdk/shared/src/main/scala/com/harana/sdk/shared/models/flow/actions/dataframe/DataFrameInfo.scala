package com.harana.sdk.shared.models.flow.actions.dataframe

import com.harana.sdk.shared.models.flow.ActionObjectInfo
import com.harana.sdk.shared.models.flow.actions.custom.SinkInfo

trait DataFrameInfo extends ActionObjectInfo {
  val id = "063B81ED-117A-4452-8774-842983E14A32"
}

object DataFrameInfo extends DataFrameInfo {
  def apply() = new DataFrameInfo {}
}