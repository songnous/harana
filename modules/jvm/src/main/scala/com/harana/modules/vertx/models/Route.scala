package com.harana.modules.vertx.models

import io.vertx.core.http.HttpMethod
import io.vertx.ext.web.RoutingContext
import zio.Task

case class Route(path: String,
                 method: HttpMethod,
                 handler: RoutingContext => Task[Response],
                 consumes: Option[ContentType] = None,
                 produces: Option[ContentType] = Some(ContentType.HTML),
                 log: Boolean = false,
                 multipart: Boolean = false,
                 secured: Boolean = false,
                 regex: Boolean = false,
                 normalisedPath: Boolean = true,
                 blocking: Boolean = false)
