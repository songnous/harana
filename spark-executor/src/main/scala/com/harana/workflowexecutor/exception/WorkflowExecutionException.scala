package com.harana.workflowexecutor.exception

case class WorkflowExecutionException(cause: Throwable)
    extends Exception(s"Execution failed: ${cause.getMessage}", cause)
