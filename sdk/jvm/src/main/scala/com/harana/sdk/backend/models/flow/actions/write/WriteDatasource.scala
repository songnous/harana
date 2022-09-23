package com.harana.sdk.backend.models.flow.actions.write

import com.harana.sdk.backend.models.flow.actionobjects.dataframe.DataFrame
import com.harana.sdk.backend.models.flow.client.datasources.DatasourceClient
import com.harana.sdk.backend.models.flow.inference.{InferContext, InferenceWarnings}
import com.harana.sdk.backend.models.flow.{Action1To0, ExecutionContext, Knowledge}
import com.harana.sdk.shared.models.flow.actions.write.WriteDatasourceInfo
import com.harana.sdk.shared.models.flow.exceptions
import com.harana.sdk.shared.models.flow.exceptions.FlowMultiError

class WriteDatasource extends Action1To0[DataFrame] with WriteDatasourceInfo {

  // FIXME
  def execute(dataFrame: DataFrame)(context: ExecutionContext) = createWriteDataFrameFromDatasource(null)
    .execute(dataFrame)(context)

  override def inferKnowledge(k0: Knowledge[DataFrame])(context: InferContext): (Unit, InferenceWarnings) = {

    // FIXME: Should not be null
    val writeDataFrame = createWriteDataFrameFromDatasource(null)

    val parametersValidationErrors = writeDataFrame.validateParameters
    if (parametersValidationErrors.nonEmpty) throw exceptions.FlowMultiError(parametersValidationErrors).toException
    writeDataFrame.inferKnowledge(k0)(context)
  }

  private def createWriteDataFrameFromDatasource(datasourceClient: DatasourceClient) = {
    new WriteDataFrame().setStorageType(null)
  }
}