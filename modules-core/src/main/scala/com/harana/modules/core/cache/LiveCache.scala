package com.harana.modules.core.cache

import com.github.benmanes.caffeine.cache.RemovalCause
import com.github.blemale.scaffeine.{Scaffeine, Cache => SCache}
import com.harana.modules.core.cache.Cache.Service
import com.harana.modules.core.config.Config
import com.harana.modules.core.logger.Logger
import com.harana.modules.core.micrometer.Micrometer

import scala.concurrent.duration._
import zio.{Task, ZLayer}

object LiveCache {
  val layer = ZLayer.fromServices { (config: Config.Service,
                                     logger: Logger.Service,
                                     micrometer: Micrometer.Service) => new Service {

    def newCache[K, V](expirationSeconds: Long, removalListener: Option[(K, V, RemovalCause) => Unit] = None): Task[SCache[K, V]] = {
      Task {
        val cache = Scaffeine().recordStats().expireAfterWrite(expirationSeconds.seconds)
        if (removalListener.nonEmpty) cache.removalListener(removalListener.get)
        cache.build[K, V]()
      }
    }
  }}
}