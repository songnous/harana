package com.harana.designer.backend.modules.projects

import com.harana.modules.vertx.models.Response
import zio.macros.accessible
import zio.{Has, Task}

@accessible
object Projects {
  type Projects = Has[Projects.Service]

  trait Service {
    def setup(namespace: String): Task[Unit]

    def startMonitoring(namespace: String): Task[Unit]

    def stopMonitoring(namespace: String): Task[Unit]
  }
}