package com.harana.s3.services.cache

import zio.macros.accessible
import zio.{Has, Task}

import java.nio.ByteBuffer

@accessible
object Cache {
  type Cache = Has[Cache.Service]

  trait Service {

    def get(key: String): Task[ByteBuffer]

    def put(key: String, content: ByteBuffer): Task[Int]

    def remove(key: String): Task[Unit]

    def containsKey(key: String): Task[Boolean]

  }
}
