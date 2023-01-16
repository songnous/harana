package com.harana.modules.core.app

import com.harana.modules.core.Layers
import com.harana.modules.core.config.Config
import com.harana.modules.core.logger.Logger
import zio.{IO, Task, UIO, ZIO}

abstract class App extends zio.App {

  def config(s: String) = Config.string(s).provideLayer(Layers.config)
  def env(s: String) = Config.env(s).provideLayer(Layers.config)
  def secret(s: String) = Config.secret(s).provideLayer(Layers.config)

  def logInfo(s: String) = Logger.info(s).provideLayer(Layers.logger)
  def logError(s: String) = Logger.error(s).provideLayer(Layers.logger)

  def startup: ZIO[Any, Any, _]
  def shutdown: UIO[Unit]

  override def run(args: List[String]) =
    for {
      cluster       <- env("harana_cluster")
      domain        <- env("harana_domain")
      environment   <- env("harana_environment")
      _             <- logInfo(s"Harana Cluster: $cluster")
      _             <- logInfo(s"Harana Domain: $domain")
      _             <- logInfo(s"Harana Environment: $environment")

                    // BROKEN - This thread just gets terminated early
      _             <- IO.effectTotal(java.lang.Runtime.getRuntime.addShutdownHook(new Thread {
                          override def run() = {
                            println("------------------------------------------------ A")
                            val _ = unsafeRunSync(shutdown)
                            println("------------------------------------------------ B")
                          }
                        }))

      exitCode      <- startup.onError(e => logError(e.prettyPrint)).exitCode
    } yield exitCode
}