package com.harana.spark.spi

import java.util.ServiceLoader
import scala.jdk.CollectionConverters._
import org.apache.spark.sql.SparkSession

/**
  * SPI Interface for services wishing to tweak the SparkSession after its created
  * (e.g. for registering UDFs).
  *
  * @since 5/22/18
  */
trait SparkSessionInitializer {
  def init(sparkSession: SparkSession): Unit
}

object SparkSessionInitializer {
  def apply(sparkSession: SparkSession): SparkSession = {

    val initializers = ServiceLoader.load(classOf[SparkSessionInitializer]).asScala
    for(initter <- initializers) {
      initter.init(sparkSession)
    }
    sparkSession
  }
}
