package com.harana.sdk.shared.models.flow.actiontypes.write

import com.harana.sdk.shared.models.flow.parameters.BooleanParameter
import com.harana.sdk.shared.models.flow.parameters.datasource.DatasourceIdForWriteParameter

trait WriteDatasourceParameters {
  val datasourceIdParameter = DatasourceIdForWriteParameter("data-source")
  val shouldOverwriteParameter = BooleanParameter("overwrite", default = Some(true))
}