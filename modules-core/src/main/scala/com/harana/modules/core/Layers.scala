package com.harana.modules.core

import com.harana.modules.core.cache.LiveCache
import com.harana.modules.core.config.LiveConfig
import com.harana.modules.core.logger.LiveLogger
import com.harana.modules.core.micrometer.LiveMicrometer
import com.harana.modules.core.okhttp.LiveOkHttp
import zio.console.Console

object Layers {
  val logger = Console.live >>> LiveLogger.layer
  val config = logger >>> LiveConfig.layer
  val standard = config ++ logger ++ LiveMicrometer.layer

  val cache = standard >>> LiveCache.layer
  val okHttp = standard >>> LiveOkHttp.layer
}