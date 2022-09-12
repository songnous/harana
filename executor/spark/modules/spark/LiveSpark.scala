package com.harana.executor.spark.modules.spark

import com.harana.executor.spark.modules.spark.Spark.Service
import com.harana.executor.spark.utils.ExtraStrategies
import org.apache.spark.sql.SparkSession
import zio.{Task, ZLayer}

object LiveSpark {
  val layer = ZLayer.succeed(new Service {

    def newSession(appName: String, config: Map[String, String] = Map()): Task[SparkSession] =
      Task {
        var builder = SparkSession.builder().appName(appName)
        config.foreach { case (k, v) => builder = builder.config(k, v) }
        val spark = builder.getOrCreate()
        ExtraStrategies.register(spark)
        spark
      }
  })
}