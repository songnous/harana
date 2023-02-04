package com.harana.modules.file

import com.harana.modules.core.logger.Logger
import com.harana.modules.file.File.Service
import com.harana.modules.vertx.models.streams.AsyncFileReadStream
import io.vertx.core.buffer.Buffer
import io.vertx.core.streams.ReadStream
import io.vertx.ext.reactivestreams.ReactiveWriteStream
import one.jasyncfio.{AsyncFile, EventExecutor}
import org.apache.commons.lang3.SystemUtils
import org.reactivestreams.{Subscriber, Subscription}
import zio.blocking._
import zio.{Has, Task, ZIO, ZLayer}

import java.io.{FileInputStream, FileOutputStream}
import java.nio.ByteBuffer
import java.nio.file.{Files, Path}

object LiveFile {
  val layer = ZLayer.fromServices { (blocking: Blocking.Service,
                                     logger: Logger.Service) => new Service {

    private val chunk_size = 1024

    private val eventExecutor =
      if (SystemUtils.IS_OS_LINUX) Some(EventExecutor.initDefault()) else None


    def readStream(path: String, range: Option[(Long, Long)] = None): Task[ReadStream[Buffer]] =
      Task(new AsyncFileReadStream(path, range))


    def read(file: Either[Path, AsyncFile], buffer: ByteBuffer, position: Option[Int] = None) = {
      file match {
        case Left(path) =>
          Task {
            val bytes = Files.readAllBytes(path)
            buffer.put(bytes)
            bytes.size
          }

        case Right(file) =>
          ZIO.fromFutureJava(if (position.nonEmpty) file.read(buffer, position.get) else file.read(buffer)).provide(Has(blocking)).map(_.toInt)
      }
    }


    def writeStream(path: String, stream: ReactiveWriteStream[Buffer], length: Long, onStart: Option[() => Any] = None, onStop: Option[() => Any]) =
      if (eventExecutor.nonEmpty)
        for {
          file    <- ZIO.fromCompletableFuture(AsyncFile.open(path, eventExecutor.get))
          _       <- Task.effectAsync[Unit](cb =>
                      stream.subscribe(new Subscriber[Buffer] {
                        var subscription: Subscription = _
                        var remaining = length

                        override def onSubscribe(sub: Subscription) = {
                          subscription = sub
                          if (onStart.nonEmpty) onStart.get.apply()
                          sub.request(if (remaining > chunk_size) chunk_size else remaining)
                        }
                        override def onNext(t: Buffer) = {
                          file.write(t.getByteBuf.nioBuffer())
                          remaining -= t.length()
                          if (remaining == 0) {
                            subscription.cancel()
                            onComplete()
                          } else
                            subscription.request(if (remaining > chunk_size) chunk_size else remaining)
                        }
                        override def onError(t: Throwable) = throw t
                        override def onComplete() = {
                          file.close()
                          if (onStop.nonEmpty) onStop.get.apply()
                          cb(Task.unit)
                        }
                      })
                  )
        } yield ()
      else
        Task.effectAsync[Unit] { cb =>
          stream.subscribe(new Subscriber[Buffer] {
            var subscription: Subscription = _
            var remaining = length
            var fos: FileOutputStream = _

            override def onSubscribe(sub: Subscription) = {
              subscription = sub
              fos = new FileOutputStream(path)
              if (onStart.nonEmpty) onStart.get.apply()
              subscription.request(if (remaining > chunk_size) chunk_size else remaining)
            }

            override def onNext(t: Buffer) = {
              fos.write(t.getBytes)
              remaining -= t.length()
              if (remaining == 0) {
                subscription.cancel()
                onComplete()
              } else
                subscription.request(if (remaining > chunk_size) chunk_size else remaining)
            }

            override def onError(t: Throwable) = cb(Task.fail(t))
            override def onComplete() = {
              fos.close()
              if (onStop.nonEmpty) onStop.get.apply()
              cb(Task.unit)
            }
          })
        }


    def write(file: Either[Path, AsyncFile], buffer: ByteBuffer, position: Option[Int] = None) =
      file match {
        case Left(path) =>
          Task {
            val array = buffer.array()
            Files.write(path, array)
            array.size
          }

        case Right(file) =>
          ZIO.fromFutureJava(if (position.nonEmpty) file.write(buffer, position.get) else file.write(buffer)).provide(Has(blocking)).map(_.toInt)
      }


    def merge(sourcePaths: List[Path], targetPath: Path): Task[Unit] =
      Task {
        val target = new FileOutputStream(targetPath.toFile, true).getChannel
        sourcePaths.foreach { path =>
          val fis = new FileInputStream(path.toFile).getChannel
          fis.transferFrom(target, Files.size(path), target.size())
          fis.close()
        }
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