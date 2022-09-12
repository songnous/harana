package com.harana.executor.spark.modules.flowexecutor

import com.harana.sdk.backend.models.designer.flow.{Flow, FlowExecution}
import zio.macros.accessible
import zio.{Has, Task}

@accessible
object FlowExecutor {
  type FlowExecutor = Has[FlowExecutor.Service]

  trait Service {
    def executeFlows: Task[Unit]
    def executeFlow(flow: Flow, flowExecution: FlowExecution): Task[Unit]
  }
}