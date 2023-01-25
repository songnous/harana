package com.harana.modules.file

import io.vertx.core.buffer.Buffer
import io.vertx.core.streams.{ReadStream, WriteStream}
import io.vertx.ext.reactivestreams.{ReactiveReadStream, ReactiveWriteStream}
import one.jasyncfio.AsyncFile
import org.reactivestreams.Publisher
import zio.macros.accessible
import zio.{Has, Task}

import java.nio.ByteBuffer
import java.nio.file.Path

@accessible
object File {
  type File = Has[File.Service]

  trait Service {

    def readStream(path: String): Task[ReadStream[Buffer]]

    def read(file: Either[Path, AsyncFile], buffer: ByteBuffer, position: Option[Int] = None): Task[Int]

    def writeStream(path: String, stream: ReactiveWriteStream[Buffer], length: Long, onStart: Option[() => Any] = None, onStop: Option[() => Any] = None): Task[Unit]

    def write(file: Either[Path, AsyncFile], buffer: ByteBuffer, position: Option[Int] = None): Task[Int]

    def close(file: Either[Path, AsyncFile]): Task[Unit]

  }
}