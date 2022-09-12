package com.harana.modules.google

import com.harana.modules.core.config.Config
import com.harana.modules.google.Google.{Event, Service}
import com.harana.modules.core.logger.Logger
import com.harana.modules.core.micrometer.Micrometer
import com.harana.modules.core.okhttp.OkHttp
import zio.{Task, ZLayer}

object LiveGoogle {
    val layer = ZLayer.fromServices { (config: Config.Service,
                                       logger: Logger.Service,
                                       micrometer: Micrometer.Service,
                                       okHttp: OkHttp.Service) => new Service {

        def pageView(clientId: String, page: String, title: String): Task[Event] =
          for {
            propertyId      <- config.string("google.tags.propertyId")
            domain          <- config.string("http.domain", "domain")
            event           <- Task(
                                  Map(
                                    "v" -> 1,
                                    "tid" -> propertyId,
                                    "cid" -> clientId,
                                    "t" -> "pageview",
                                    "dh" -> domain,
                                    "dp" -> (if (page.startsWith("/")) page else s"/$page"),
                                    "dt" -> title,
                                  ).mkString("&")
                                )
          } yield event


        def event(clientId: String, category: String, action: String, label: String, value: String): Task[Event] =
          for {
            propertyId      <- config.string("google.tags.propertyId")
            event           <- Task(
                                  Map(
                                    "v" -> 1,
                                    "tid" -> propertyId,
                                    "cid" -> clientId,
                                    "t" -> "event",
                                    "ec" -> category,
                                    "ea" -> action,
                                    "el" -> label,
                                    "ev" -> value
                                  ).mkString("&")
                                )
          } yield event


        def exception(clientId: String, description: String, fatal: Boolean): Task[Event] =
          for {
              propertyId      <- config.string("google.tags.propertyId")
              event           <- Task(
                                  Map(
                                    "v" -> 1,
                                    "tid" -> propertyId,
                                    "cid" -> clientId,
                                    "t" -> "event",
                                    "exd" -> description,
                                    "exf" -> (if (fatal) 1 else 0)
                                  ).mkString("&")
                                )
          } yield event


        def time(clientId: String, category: String, variable: String, time: Long, label: String): Task[Event] =
          for {
              propertyId      <- config.string("google.tags.propertyId")
              event           <- Task(
                                  Map(
                                    "v" -> 1,
                                    "tid" -> propertyId,
                                    "cid" -> clientId,
                                    "t" -> "timing",
                                    "utc" -> category,
                                    "utv" -> variable ,
                                    "utt" -> time,
                                    "utl" -> label
                                  ).mkString("&")
                                )
          } yield event


        def send(event: Event): Task[Unit] =
          for {
              url             <- config.string("google.tags.url")
              _               <- okHttp.post(s"$url/collect", Some(event)).mapError(e => new Exception(e.toString))
          } yield ()


        def batch(events: List[Event]): Task[Unit] =
          for {
              url             <- config.string("google.tags.url")
              _               <- okHttp.post(s"$url/batch", Some(events.mkString("\n"))).mapError(e => new Exception(e.toString))
          } yield ()
    }}
}