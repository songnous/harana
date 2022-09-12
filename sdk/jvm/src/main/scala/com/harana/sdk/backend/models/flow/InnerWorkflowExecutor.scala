package com.harana.sdk.backend.models.flow

import com.harana.sdk.backend.models.flow.actionobjects.dataframe.DataFrame
import com.harana.sdk.shared.models.flow.parameters.custom.InnerWorkflow
import io.circe.Json

trait InnerWorkflowExecutor {

  def parse(workflow: Json): InnerWorkflow

  def toJson(innerWorkflow: InnerWorkflow): Json

  def execute(executionContext: CommonExecutionContext, workflow: InnerWorkflow, dataFrame: DataFrame): DataFrame

}
