package com.harana.modules.jasyncfio

import io.vertx.core.buffer.Buffer
import io.vertx.ext.reactivestreams.{ReactiveReadStream, ReactiveWriteStream}
import one.jasyncfio.AsyncFile
import zio.macros.accessible
import zio.{Has, Task}

import java.nio.ByteBuffer

@accessible
object Jasyncfio {
  type Jasyncfio = Has[Jasyncfio.Service]

  trait Service {

    def open(path: String): Task[AsyncFile]

    def readStream(path: String,  readStream: ReactiveReadStream[Buffer]): Task[Unit]

    def read(file: AsyncFile, buffer: ByteBuffer, position: Option[Int] = None): Task[Int]

    def writeStream(path: String, writeStream: ReactiveWriteStream[Buffer]): Task[Unit]

    def write(file: AsyncFile, buffer: ByteBuffer, position: Option[Int] = None): Task[Int]

    def close(file: AsyncFile): Task[Unit]

  }
}