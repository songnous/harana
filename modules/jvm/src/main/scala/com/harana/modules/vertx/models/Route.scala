package com.harana.modules.vertx.models

import io.vertx.core.http.HttpMethod
import io.vertx.ext.web.RoutingContext
import zio.Task

case class Route(path: String,
                 method: HttpMethod,
                 handler: RoutingContext => Task[Response],
                 consumes: Option[ContentType] = None,
                 produces: Option[ContentType] = Some(ContentType.HTML),
                 isSecured: Boolean = false,
                 isRegex: Boolean = false,
                 isNormalisedPath: Boolean = true,
                 isBlocking: Boolean = true)
