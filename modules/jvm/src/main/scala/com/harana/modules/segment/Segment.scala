package com.harana.modules.segment

import com.harana.modules.segment.models.SegmentOptions
import zio.macros.accessible
import zio.{Has, IO}

@accessible
object Segment {
  type Segment = Has[Segment.Service]

  trait Service {
    def alias(previousId: String, userId: String, options: SegmentOptions): IO[Nothing, Unit]

    def group(userId: String, groupId: String, traits: Map[String, _], options: SegmentOptions): IO[Nothing, Unit]

    def identify(userId: String, traits: Map[String, _], options: SegmentOptions): IO[Nothing, Unit]

    def page(userId: String, name: String, properties: Map[String, _], options: SegmentOptions): IO[Nothing, Unit]

    def screen(userId: String, name: String, properties: Map[String, _], options: SegmentOptions): IO[Nothing, Unit]

    def track(userId: String, event: String, properties: Map[String, _], options: SegmentOptions): IO[Nothing, Unit]

    def flush: IO[Nothing, Unit]
  }
}