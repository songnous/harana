package com.harana.designer.backend.services.setup

import com.harana.id.jwt.shared.models.DesignerClaims
import zio.macros.accessible
import zio.{Has, Task}

@accessible
object Setup {
  type Setup = Has[Setup.Service]

  trait Service {
    def createApps(claims: DesignerClaims): Task[Unit]

    def provisionSampleData: Task[Unit]
  }
}