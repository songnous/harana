package com.harana.sdk.backend.models.flow.actiontypes.read

import com.harana.sdk.backend.models.flow.ExecutionContext
import com.harana.sdk.backend.models.flow.actionobjects.dataframe.DataFrame
import com.harana.sdk.backend.models.flow.actiontypes.ActionTypeType0To1
import com.harana.sdk.shared.models.flow.actiontypes.read.ReadDatasourceInfo
import izumi.reflect.Tag

class ReadDatasource extends ActionTypeType0To1[DataFrame] with ReadDatasourceInfo {

  def execute()(context: ExecutionContext) =
    null

  lazy val tTagTO_0: Tag[DataFrame] = typeTag

}