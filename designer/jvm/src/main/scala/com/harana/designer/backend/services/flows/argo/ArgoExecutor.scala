package com.harana.designer.backend.services.flows.argo

import com.harana.sdk.shared.models.common.User.UserId
import com.harana.sdk.backend.models.designer.flow.{ActionType, Flow}
import zio.macros.accessible
import zio.{Has, Task}

@accessible
object ArgoExecutor {
  type ArgoExecutor = Has[ArgoExecutor.Service]

  trait Service {
    def actionTypes: List[ActionType]

    def deploy(flow: Flow, userId: UserId): Task[Unit]
    def undeploy(flow: Flow, userId: UserId): Task[Unit]
  }
}
