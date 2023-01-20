package com.harana.modules.file

import io.vertx.core.buffer.Buffer
import io.vertx.ext.reactivestreams.{ReactiveReadStream, ReactiveWriteStream}
import one.jasyncfio.AsyncFile
import zio.macros.accessible
import zio.{Has, Task}

import java.nio.ByteBuffer
import java.nio.file.Path

@accessible
object File {
  type File = Has[File.Service]

  trait Service {

    def open(path: String): Task[Either[Path, AsyncFile]]

    def readStream(path: String,  readStream: ReactiveReadStream[Buffer]): Task[Unit]

    def read(file: Either[Path, AsyncFile], buffer: ByteBuffer, position: Option[Int] = None): Task[Int]

    def writeStream(path: String, writeStream: ReactiveWriteStream[Buffer]): Task[Unit]

    def write(file: Either[Path, AsyncFile], buffer: ByteBuffer, position: Option[Int] = None): Task[Int]

    def close(file: Either[Path, AsyncFile]): Task[Unit]

  }
}