package com.harana.designer.frontend.utils.http

import sttp.capabilities.Effect
import sttp.client3._
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util._

trait MetricsServer {
  def reportDuration(method: String, name: String, duration: Long): Unit
}

class CloudMetricsServer extends MetricsServer {
  override def reportDuration(method: String, uri: String, duration: Long): Unit = {
    //println(s"$method $uri -> ${duration}ms")
  }
}

// the backend wrapper
class MetricWrapper[P](delegate: SttpBackend[Future, P], metrics: MetricsServer) extends DelegateSttpBackend[Future, P](delegate) {

  override def send[T, R >: P with Effect[Future]](request: Request[T, R]): Future[Response[T]] = {
    val start = System.currentTimeMillis()

    def report(metricSuffix: String): Unit = {
      val end = System.currentTimeMillis()
      metrics.reportDuration(request.method.toString(), request.uri.toString(), end - start)
    }

    delegate.send(request).andThen {
      case Success(response) if response.is200 => report("ok")
      case Success(response)                   => report("notok")
      case Failure(t)                          => report("exception")
    }
  }

  override def close() = delegate.close()
}
