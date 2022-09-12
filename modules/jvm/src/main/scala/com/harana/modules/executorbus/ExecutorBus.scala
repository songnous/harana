package com.harana.modules.executorbus

import com.harana.sdk.shared.models.designer.flow.flows.Workflow
import com.harana.sdk.shared.models.flow.utils.Id
import io.circe.{Decoder, Encoder}
import zio.{Has, Task}
import zio.macros.accessible

@accessible
object ExecutorBus {
  type EventBus = Has[ExecutorBus.Service]

  trait Service {

    def publishGlobalEvent[T](event: T)(implicit encoder: Encoder[T]): Task[Unit]
    def waitForGlobalEvent[T](timeout: Option[Int])(implicit decoder: Decoder[T]): Task[T]

    def publishWorkflowEvent[T](workflow: Id, event: T)(implicit encoder: Encoder[T]): Task[Unit]
    def waitForWorkflowEvent[T](workflow: Id, timeout: Option[Int])(implicit decoder: Decoder[T]): Task[T]

  }
}