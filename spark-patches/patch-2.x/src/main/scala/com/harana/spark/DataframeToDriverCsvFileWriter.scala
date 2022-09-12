package org.apache.spark.sql.execution.datasources.csv

import com.harana.spark.readwritedataframe.ManagedResource
import org.apache.spark.sql.{DataFrame, SparkSession}
import org.apache.spark.sql.types._

object DataframeToDriverCsvFileWriter {

  def write(
      dataFrame: DataFrame,
      options: Map[String, String],
      dataSchema: StructType,
      pathWithoutScheme: String,
      sparkSession: SparkSession
  ): Unit = {
    val data   = dataFrame.rdd.collect()
    val params = MapToCsvOptions(options, sparkSession.sessionState.conf)
    ManagedResource(
      new LocalCsvOutputWriter(dataSchema, params, pathWithoutScheme)
    )(writer => data.foreach(row => writer.write(row.toSeq.map(_.asInstanceOf[String]))))
  }

}
