package com.harana.modules.zookeeper

import zio.{Has, Task}
import zio.macros.accessible

@accessible
object Zookeeper {
  type Zookeeper = Has[Zookeeper.Service]

  trait Service {

    def localStart: Task[Unit]

    def localStop: Task[Unit]

  }
}