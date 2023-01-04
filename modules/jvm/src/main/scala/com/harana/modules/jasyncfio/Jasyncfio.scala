package com.harana.modules.jasyncfio

import one.jasyncfio.AsyncFile
import zio.macros.accessible
import zio.{Has, Task}

import java.nio.ByteBuffer

@accessible
object Jasyncfio {
  type Jasyncfio = Has[Jasyncfio.Service]

  trait Service {

    def open(path: String): Task[AsyncFile]

    def read(file: AsyncFile, buffer: ByteBuffer, position: Option[Int] = None): Task[Int]

    def write(file: AsyncFile, buffer: ByteBuffer, position: Option[Int] = None): Task[Int]

    def close(file: AsyncFile): Task[Unit]

  }
}