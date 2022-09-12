package com.harana.executor.spark.modules.spark

import org.apache.spark.sql.SparkSession
import zio.macros.accessible
import zio.Has
import zio.Task

@accessible
object Spark {
  type Spark = Has[Spark.Service]

  trait Service {
    def newSession(appName: String, config: Map[String, String] = Map()): Task[SparkSession]
  }
}