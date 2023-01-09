package com.harana.modules.jasyncfio

import com.harana.modules.jasyncfio.Jasyncfio.Service
import io.vertx.core.buffer.Buffer
import io.vertx.ext.reactivestreams.{ReactiveReadStream, ReactiveWriteStream}
import one.jasyncfio.{AsyncFile, EventExecutor}
import org.reactivestreams.{Subscriber, Subscription}
import zio.blocking._
import zio.{Has, Task, ZIO, ZLayer}

import java.nio.{ByteBuffer, ByteOrder}

object LiveJasyncfio {
  val layer = ZLayer.fromService { (blocking: Blocking.Service) => new Service {

    private val eventExecutor = EventExecutor.initDefault()

    def open(path: String) =
      ZIO.fromFutureJava(AsyncFile.open(path, eventExecutor)).provide(Has(blocking))

    def readStream(path: String, readStream: ReactiveReadStream[Buffer]) =
      Task {
        val file = AsyncFile.open(path, eventExecutor).join()
        var read = -1
        val buffer = ByteBuffer.allocateDirect(Integer.BYTES).order(ByteOrder.nativeOrder())

        while (read > 0 && read != -1) {
          read = file.read(buffer).join()
          readStream.onNext(Buffer.buffer(buffer.array()))
        }
        readStream.onComplete()
        file.close().join()
      }

    def read(file: AsyncFile, buffer: ByteBuffer, position: Option[Int] = None) =
      ZIO.fromFutureJava(if (position.isDefined) file.read(buffer, position.get) else file.read(buffer)).provide(Has(blocking)).map(_.toInt)

    def writeStream(path: String, writeStream: ReactiveWriteStream[Buffer]) =
      Task {
        val file = AsyncFile.open(path, eventExecutor).join()
        writeStream.subscribe(new Subscriber[Buffer] {
            override def onSubscribe(sub: Subscription) = {}
            override def onNext(t: Buffer) = file.write(t.getByteBuf.nioBuffer())
            override def onError(t: Throwable) = throw t
            override def onComplete() = file.close()
          })
        file.close().join()
      }

    def write(file: AsyncFile, buffer: ByteBuffer, position: Option[Int] = None) =
      ZIO.fromFutureJava(if (position.isDefined) file.write(buffer, position.get) else file.write(buffer)).provide(Has(blocking)).map(_.toInt)

    def close(file: AsyncFile) =
      ZIO.fromFutureJava(file.close()).provide(Has(blocking)).map(_.toInt)

  }}
}