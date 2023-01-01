package com.harana.s3.services

import zio.macros.accessible
import zio.{Has, Task}

@accessible
object S3 {
  type Ognl = Has[S3.Service]

  trait Service {
    def render(expression: String, context: Map[String, Any]): Task[Any]
  }
}