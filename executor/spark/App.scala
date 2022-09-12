package com.harana.executor.spark

import com.harana.Layers
import com.harana.modules.core.{Layers => CoreLayers}
import com.harana.modules.mongo.LiveMongo
import com.harana.executor.spark.modules.flowexecutor.{FlowExecutor, LiveFlowExecutor}
import com.harana.executor.spark.modules.spark.LiveSpark
import zio._
import zio.clock.Clock

object App extends zio.App {

  System.setProperty("isThreadContextMapInheritable", "true")

  val flowExecutor = (Clock.live ++ CoreLayers.standard ++ Layers.mongo ++ LiveSpark.layer ++ Layers.vertx) >>> LiveFlowExecutor.layer

  def run(args: List[String]): ZIO[zio.ZEnv, Nothing, zio.ExitCode] =
    FlowExecutor
      .executeFlows
      .provideLayer(flowExecutor)
      .fork
      .whenM(UIO(true))
      .repeat(everySecond)
      .exitCode
}