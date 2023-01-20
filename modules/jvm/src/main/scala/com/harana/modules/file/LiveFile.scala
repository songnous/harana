package com.harana.modules.file

import com.harana.modules.file.File.Service
import io.vertx.core.buffer.Buffer
import io.vertx.ext.reactivestreams.{ReactiveReadStream, ReactiveWriteStream}
import one.jasyncfio.{AsyncFile, EventExecutor}
import org.apache.commons.lang3.SystemUtils
import org.reactivestreams.{Subscriber, Subscription}
import zio.blocking._
import zio.{Has, Task, ZIO, ZLayer}

import java.nio.file.{Files, Path, Paths, StandardOpenOption}
import java.nio.{ByteBuffer, ByteOrder}

object LiveFile {
  val layer = ZLayer.fromService { (blocking: Blocking.Service) => new Service {

    private val eventExecutor =
      if (SystemUtils.IS_OS_LINUX) Some(EventExecutor.initDefault()) else None

    def open(path: String) = {
      if (eventExecutor.isDefined)
        ZIO.fromFutureJava(AsyncFile.open(path, eventExecutor.get)).provide(Has(blocking)).map(Right(_))
      else
        Task(Left(Paths.get(path)))
    }


    def readStream(path: String, readStream: ReactiveReadStream[Buffer]) =
      if (eventExecutor.isDefined)
        Task {
          val file = AsyncFile.open(path, eventExecutor.get).join()
          var read = -1
          val buffer = ByteBuffer.allocateDirect(Integer.BYTES).order(ByteOrder.nativeOrder())

          while (read > 0 && read != -1) {
            read = file.read(buffer).join()
            readStream.onNext(Buffer.buffer(buffer.array()))
          }
          readStream.onComplete()
          file.close().join()
        }
      else
        Task {
          readStream.onNext(Buffer.buffer(Files.readAllBytes(Paths.get(path))))
          readStream.onComplete()
        }


    def read(file: Either[Path, AsyncFile], buffer: ByteBuffer, position: Option[Int] = None) = {
      file match {
        case Left(path) =>
          Task {
            val bytes = Files.readAllBytes(path)
            buffer.put(bytes)
            bytes.size
          }

        case Right(file) =>
          ZIO.fromFutureJava(if (position.isDefined) file.read(buffer, position.get) else file.read(buffer)).provide(Has(blocking)).map(_.toInt)
      }
    }


    def writeStream(path: String, writeStream: ReactiveWriteStream[Buffer]) =
      if (eventExecutor.isDefined)
        Task {
          val file = AsyncFile.open(path, eventExecutor.get).join()
          writeStream.subscribe(new Subscriber[Buffer] {
              override def onSubscribe(sub: Subscription) = {}
              override def onNext(t: Buffer) = file.write(t.getByteBuf.nioBuffer())
              override def onError(t: Throwable) = throw t
              override def onComplete() = file.close()
            })
          file.close().join()
        }
      else
        Task(
          writeStream.subscribe(new Subscriber[Buffer] {
              override def onSubscribe(sub: Subscription) = {}
              override def onNext(t: Buffer) = Files.write(Paths.get(path), t.getBytes, StandardOpenOption.APPEND)
              override def onError(t: Throwable) = throw t
              override def onComplete() = {}
            })
        )


    def write(file: Either[Path, AsyncFile], buffer: ByteBuffer, position: Option[Int] = None) =
      file match {
        case Left(path) =>
          Task {
            val array = buffer.array()
            Files.write(path, array)
            array.size
          }

        case Right(file) =>
          ZIO.fromFutureJava(if (position.isDefined) file.write(buffer, position.get) else file.write(buffer)).provide(Has(blocking)).map(_.toInt)
      }


    def close(file: Either[Path, AsyncFile]) =
      file match {
        case Left(path) =>
          Task.unit
        case Right(file) =>
          ZIO.fromFutureJava(file.close()).provide(Has(blocking)).map(_.toInt)
      }
  }}
}