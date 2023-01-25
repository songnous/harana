package com.harana.modules.vertx

import io.vertx.core.buffer.Buffer
import io.vertx.core.http.HttpHeaders
import io.vertx.core.streams.Pump
import io.vertx.ext.reactivestreams.ReactiveWriteStream
import io.vertx.ext.web.RoutingContext
import org.reactivestreams.{Subscriber, Subscription}
import zio.Task

import scala.collection.mutable.ArrayBuffer

object VertxUtils {

    def streamToString(rc: RoutingContext, stream: ReactiveWriteStream[Buffer]) =
    Task.effectAsync[String](cb => stream.subscribe(new Subscriber[Buffer] {
      val bytes = ArrayBuffer.empty[Byte]
      def onSubscribe(s: Subscription) = {
        println("STS: OnSubscribe")
        val length = rc.request().getHeader(HttpHeaders.CONTENT_LENGTH).toLong
        if (length == 0) cb(Task("")) else s.request(rc.request().getHeader(HttpHeaders.CONTENT_LENGTH).toLong)
      }
      def onNext(t: Buffer) = bytes.addAll(t.getBytes)
      def onError(t: Throwable) = cb(Task.fail(t))
      def onComplete() = {
        cb(Task(new String(bytes.toArray)))
      }
    }))

}
