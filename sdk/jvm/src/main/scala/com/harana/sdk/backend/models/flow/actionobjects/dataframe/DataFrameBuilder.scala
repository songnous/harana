package com.harana.sdk.backend.models.flow.actionobjects.dataframe

import com.harana.spark.SparkSQLSession
import org.apache.spark.rdd.RDD
import org.apache.spark.sql
import org.apache.spark.sql.Row
import org.apache.spark.sql.types.StructType

case class DataFrameBuilder(sparkSQLSession: SparkSQLSession) {

  def buildDataFrame(schema: StructType, data: RDD[Row]) = {
    val dataFrame: sql.DataFrame = sparkSQLSession.createDataFrame(data, schema)
    DataFrame.fromSparkDataFrame(dataFrame)
  }
}

object DataFrameBuilder
