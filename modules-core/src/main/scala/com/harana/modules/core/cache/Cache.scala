package com.harana.modules.core.cache

import com.github.benmanes.caffeine.cache.RemovalCause
import com.github.blemale.scaffeine.{Cache => SCache}
import zio.macros.accessible
import zio.{Has, Task}

@accessible
object Cache {
  type Caffeine = Has[Cache.Service]

  trait Service {
    def newCache[K, V](expirationSeconds: Long, removalListener: Option[(K, V, RemovalCause) => Unit] = None): Task[SCache[K, V]]
  }
}