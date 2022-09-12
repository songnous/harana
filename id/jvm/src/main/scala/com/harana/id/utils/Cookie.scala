package com.harana.id.utils

import com.harana.modules.core.config.Config
import io.vertx.core.http.{CookieSameSite, Cookie => VertxCookie }
import zio.Task

object Cookie {

  def temporary(config: Config.Service, name: String, value: String, path: String): Task[VertxCookie] = {
    for {
      domain                <- config.env("harana_domain")
      ssl                   <- config.boolean("http.ssl")
      cookieTimeout         <- config.int("signup.cookie.timeout", 60)
      cookie                <- Task {
                                val cookie = VertxCookie.cookie(name, value)
                                cookie.setDomain(domain)
                                cookie.setHttpOnly(true)
                                cookie.setMaxAge(cookieTimeout * 60)
                                cookie.setPath(s"/$path")
                                cookie.setSameSite(CookieSameSite.STRICT)
                                cookie.setSecure(ssl)
                              }
    } yield cookie
  }
}