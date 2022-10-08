package com.harana.designer.backend.services.schedules.argo

import com.harana.sdk.shared.models.common.User.UserId
import com.harana.sdk.shared.models.schedules.Schedule
import zio.macros.accessible
import zio.{Has, Task}

@accessible
object ArgoScheduler {
  type ArgoScheduler = Has[ArgoScheduler.Service]

  trait Service {
//    def deploy(schedule: Schedule, userId: UserId): Task[Unit]
//    def undeploy(schedule: Schedule, userId: UserId): Task[Unit]
  }
}