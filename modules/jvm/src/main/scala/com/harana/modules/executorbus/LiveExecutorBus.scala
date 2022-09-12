package com.harana.modules.executorbus

import com.harana.modules.core.config.Config
import com.harana.modules.core.logger.Logger
import com.harana.modules.core.micrometer.Micrometer
import com.harana.modules.executorbus.ExecutorBus.Service
import com.harana.modules.mongo.Mongo
import zio.ZLayer

object LiveExecutorBus {
  val layer = ZLayer.fromServices { (config: Config.Service,
                                     logger: Logger.Service,
                                     micrometer: Micrometer.Service,
                                     mongo: Mongo.Service) => new Service {


  }
}