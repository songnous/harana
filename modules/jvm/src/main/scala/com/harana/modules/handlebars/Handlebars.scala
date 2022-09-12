package com.harana.modules.handlebars

import zio.macros.accessible
import zio.{Has, Task, UIO}

import scala.concurrent.duration.{Duration, FiniteDuration}

@accessible
object Handlebars {
  type Handlebars = Has[Handlebars.Service]

  trait Service {

    def renderPath(path: String, props: Map[String, Object]): Task[String]

    def renderString(name: String, props: Map[String, Object]): Task[String]
  }
}