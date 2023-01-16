package com.harana.modules.ohc

import org.caffinitas.ohc.{CacheLoader, DirectValueAccess, OHCache, OHCacheBuilder}
import zio.blocking.Blocking
import zio.{Has, UIO, ZIO, ZLayer}

import java.nio.ByteBuffer
import scala.jdk.CollectionConverters._

object LiveOHC {
  val layer = ZLayer.fromService { (blocking: Blocking.Service) =>
    new OHC.Service {

      def newCache[K, V](hashTableSize: Option[Int] = None,
                         chunkSize: Option[Int] = None,
                         capacity: Option[Long] = None,
                         segmentCount: Option[Int] = None) = {
        val builder = OHCacheBuilder.newBuilder[K, V]()
        if (hashTableSize.isDefined) builder.hashTableSize(hashTableSize.get)
        if (chunkSize.isDefined) builder.chunkSize(chunkSize.get)
        if (capacity.isDefined) builder.capacity(capacity.get)
        if (segmentCount.isDefined) builder.segmentCount(segmentCount.get)
        UIO(builder.build())
      }

      def put[K, V](cache: OHCache[K, V], key: K, value: V, expireAt: Option[Long] = None) =
        UIO(if (expireAt.isDefined) cache.put(key, value, expireAt.get) else cache.put(key, value))


      def putIfAbsent[K, V](cache: OHCache[K, V], key: K, value: V, expireAt: Option[Long] = None) =
        UIO(if (expireAt.isDefined) cache.put(key, value, expireAt.get) else cache.putIfAbsent(key, value))


      def putAll[K, V](cache: OHCache[K, V], values: Map[K, V]) =
        UIO(cache.putAll(values.asJava))


      def addOrReplace[K, V](cache: OHCache[K, V], key: K, oldValue: V, newValue: V, expireAt: Option[Long] = None) =
        UIO(if (expireAt.isDefined) cache.addOrReplace(key, oldValue, newValue, expireAt.get) else cache.addOrReplace(key, oldValue, newValue))


      def remove[K, V](cache: OHCache[K, V], key: K) =
        UIO(cache.remove(key))


      def removeAll[K, V](cache: OHCache[K, V], keys: Set[K]) =
        UIO(cache.removeAll(keys.asJava))


      def clear[K, V](cache: OHCache[K, V]) =
        UIO(cache.clear())


      def get[K, V](cache: OHCache[K, V], key: K) =
        UIO(cache.get(key))


      def getAsBytes[K, V](cache: OHCache[K, V], key: K, updateLRU: Boolean = false) =
        UIO.bracket[DirectValueAccess, ByteBuffer](UIO(cache.getDirect(key, updateLRU)), d => UIO(d.close()), d => UIO(d.buffer()))


      def getWithLoader[K, V](cache: OHCache[K, V], key: K, loader: CacheLoader[K, V], expireAt: Option[Long] = None) =
        ZIO.fromFutureJava(
          if (expireAt.isDefined) cache.getWithLoaderAsync(key, loader, expireAt.get) else cache.getWithLoaderAsync(key, loader)
        ).provide(Has(blocking)).orDie


      def containsKey[K, V](cache: OHCache[K, V], key: K) =
        UIO(cache.containsKey(key))


      def size[K, V](cache: OHCache[K, V]) =
        UIO(cache.size())

    }
  }
}