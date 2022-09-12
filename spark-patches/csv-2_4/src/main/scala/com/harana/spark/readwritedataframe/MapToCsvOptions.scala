package org.apache.spark.sql.execution.datasources.csv

import org.apache.spark.sql.catalyst.csv.CSVOptions
import org.apache.spark.sql.internal.SQLConf


object MapToCsvOptions {
  def apply(option: Map[String, String], sqlConf: SQLConf) =
    new CSVOptions(option, true, sqlConf.sessionLocalTimeZone)
}
