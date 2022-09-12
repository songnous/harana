package com.harana.modules.calcite

import com.harana.sdk.shared.models.common.User.UserId
import zio.macros.accessible
import zio.{Has, Task}

@accessible
object Calcite {
  type Calcite = Has[Calcite.Service]

  trait Service {
    def rewrite(userId: String, query: String): Task[String]
  }
}