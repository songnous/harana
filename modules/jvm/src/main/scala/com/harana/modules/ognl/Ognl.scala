package com.harana.modules.ognl

import zio.macros.accessible
import zio.{Has, Task}

@accessible
object Ognl {
  type Ognl = Has[Ognl.Service]

  trait Service {
    def render(expression: String, context: Map[String, Any]): Task[Any]
  }
}