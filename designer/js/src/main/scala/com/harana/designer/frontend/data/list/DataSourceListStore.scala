package com.harana.designer.frontend.data.list

import com.harana.sdk.shared.models.data.SyncDirection
import com.harana.sdk.shared.models.flow.parameters.StringParameter

object DataSourceListStore {

  case class DataSourceEditState(dataSourceTypes: Map[String, List[String]])

}