package com.harana.modules.jasyncfio

import com.harana.modules.core.config.Config
import com.harana.modules.jasyncfio.Jasyncfio.Service
import one.jasyncfio.{AsyncFile, EventExecutor}
import zio.{Has, ZIO, ZLayer}
import zio.blocking._

import java.nio.ByteBuffer

object LiveJasyncfio {
  val layer = ZLayer.fromService { (blocking: Blocking.Service) => new Service {

    private val eventExecutor = EventExecutor.initDefault()

    def open(path: String) =
      ZIO.fromFutureJava(AsyncFile.open(path, eventExecutor)).provide(Has(blocking))

    def read(file: AsyncFile, buffer: ByteBuffer, position: Option[Int] = None) =
      ZIO.fromFutureJava(if (position.isDefined) file.read(buffer, position.get) else file.read(buffer)).provide(Has(blocking)).map(_.toInt)

    def write(file: AsyncFile, buffer: ByteBuffer, position: Option[Int] = None) =
      ZIO.fromFutureJava(if (position.isDefined) file.write(buffer, position.get) else file.write(buffer)).provide(Has(blocking)).map(_.toInt)

    def close(file: AsyncFile) =
      ZIO.fromFutureJava(file.close()).provide(Has(blocking)).map(_.toInt)

  }}
}