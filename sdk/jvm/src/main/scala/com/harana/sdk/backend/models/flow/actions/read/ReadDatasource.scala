package com.harana.sdk.backend.models.flow.actions.read

import com.harana.sdk.backend.models.flow.{ActionType0To1, ExecutionContext}
import com.harana.sdk.backend.models.flow.actionobjects.dataframe.DataFrame
import com.harana.sdk.shared.models.flow.actions.read.ReadDatasourceInfo
import scala.reflect.runtime.universe.TypeTag

class ReadDatasource extends ActionType0To1[DataFrame] with ReadDatasourceInfo {

  def execute()(context: ExecutionContext) =
    null

  lazy val tTagTO_0: TypeTag[DataFrame] = typeTag

}