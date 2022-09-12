package com.harana.sdk.shared.models.flow.actions.write

import com.harana.sdk.shared.models.flow.parameters.BooleanParameter
import com.harana.sdk.shared.models.flow.parameters.datasource.DatasourceIdForWriteParameter

trait WriteDatasourceParameters {
    val datasourceIdParameter = DatasourceIdForWriteParameter(name = "data source", description = None)
    val shouldOverwriteParameter = BooleanParameter("overwrite", Some("Should data be overwritten?"))
  }
