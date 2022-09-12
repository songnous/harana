package com.harana.sdk.backend.models.flow

trait CustomCodeExecutor {

  def isValid(code: String): Boolean

  def run(workflowId: String, nodeId: String, code: String): Unit

}
