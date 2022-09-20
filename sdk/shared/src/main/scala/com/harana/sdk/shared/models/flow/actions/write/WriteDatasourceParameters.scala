package com.harana.sdk.shared.models.flow.actions.write

import com.harana.sdk.shared.models.flow.parameters.BooleanParameter
import com.harana.sdk.shared.models.flow.parameters.datasource.DatasourceIdForWriteParameter

trait WriteDatasourceParameters {
    val datasourceIdParameter = DatasourceIdForWriteParameter("data source")
    val shouldOverwriteParameter = BooleanParameter("overwrite")
  }
