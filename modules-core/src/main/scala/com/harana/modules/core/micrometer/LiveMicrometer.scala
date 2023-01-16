package com.harana.modules.core.micrometer

import java.util.concurrent.atomic.AtomicReference
import com.harana.modules.core.micrometer.Micrometer.Service
import io.micrometer.core.instrument.Timer.Sample
import io.micrometer.core.instrument.binder.jvm._
import io.micrometer.core.instrument.binder.logging._
import io.micrometer.core.instrument.binder.system._
import io.micrometer.core.instrument.search.{RequiredSearch, Search}
import io.micrometer.core.instrument._
import io.micrometer.prometheus.{PrometheusConfig, PrometheusMeterRegistry}
import io.github.mweirauch.micrometer.jvm.extras._
import zio.{Task, UIO, ZLayer}

import scala.jdk.CollectionConverters._


object LiveMicrometer {
  private val registryRef = new AtomicReference[Option[MeterRegistry]](None)

  val layer = ZLayer.succeed(new Service {

    def config: Task[MeterRegistry#Config] =
      for {
        r <- registry
        s <- Task.effect(r.config)
      } yield s


    def clear: Task[Unit] =
      for {
        r <- registry
        s <- Task.effect(r.clear())
      } yield s


    def counter(name: String, tags: Map[String, String] = Map()): Task[Counter] =
      for {
        r <- registry
        s <- Task.effect(r.counter(name, toTags(tags)))
      } yield s


    def find(name: String): Task[Search] =
      for {
        r <- registry
        s <- Task.effect(r.find(name))
      } yield s


//    def gauge[T <: Number](name: String, tags: Map[String, String] = Map(), number: T): Task[T] =
//      for {
//        r <- registry
//        s <- Task.effect(r.gauge(name, toTags(tags), number))
//      } yield s


    def get(name: String): Task[RequiredSearch] =
      for {
        r <- registry
        s <- Task.effect(r.get(name))
      } yield s


    def getMeters: Task[List[Meter]] =
      for {
        r <- registry
        s <- Task.effect(r.getMeters.asScala.toList)
      } yield s


    def registry: UIO[MeterRegistry] =
      UIO {
        if (registryRef.get.isDefined) 
          registryRef.get.get 
        else {
          val registry = new PrometheusMeterRegistry(PrometheusConfig.DEFAULT)
          new ClassLoaderMetrics().bindTo(registry)
          new FileDescriptorMetrics().bindTo(registry)
          new JvmCompilationMetrics().bindTo(registry)
          new JvmGcMetrics().bindTo(registry)
          new JvmHeapPressureMetrics().bindTo(registry)
          new JvmMemoryMetrics().bindTo(registry)
          new JvmThreadMetrics().bindTo(registry)
          new Log4j2Metrics().bindTo(registry)
          new ProcessorMetrics().bindTo(registry)
          new ProcessMemoryMetrics().bindTo(registry)
          new ProcessThreadMetrics().bindTo(registry)
          new UptimeMetrics().bindTo(registry)

          Metrics.addRegistry(registry)
          registryRef.set(Some(registry))
          registry
        }
      }


    def startTimer: Task[Sample] =
      for {
        r <- registry
        s <- Task.effect(Timer.start(r))
      } yield s


    def stopTimer(sample: Sample, name: String, tags: Map[String, String] = Map()): Task[Long] =
      for {
        r <- registry
        s <- Task.effect(sample.stop(r.timer(name, toTags(tags))))
      } yield s


    def summary(name: String, tags: Map[String, String] = Map()): Task[DistributionSummary] =
      for {
        r <- registry
        s <- Task.effect(r.summary(name, toTags(tags)))
      } yield s


    def timer(name: String, tags: Map[String, String] = Map()): Task[Timer] =
      for {
        r <- registry
        s <- Task.effect(r.timer(name, toTags(tags)))
      } yield s

    @inline
    private def toTags(map: Map[String, String]) =
      map.map { case (k, v) => Tag.of(k, v) }.asJava
  })
}