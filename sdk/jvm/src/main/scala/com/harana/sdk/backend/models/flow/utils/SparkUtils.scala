package com.harana.sdk.backend.models.flow.utils

import com.harana.sdk.backend.models.designer.flow.utils.aggregators.CountOccurrencesWithKeyLimitAggregator
import org.apache.spark.rdd.RDD

import scala.collection._

/** Utils for spark. */
object SparkUtils {

  /** Counts occurrences of different values and outputs result as Map[T,Long]. Amount of keys in result map is limited by `limit`.
    * @return
    *   `None` if amount of distinct values is bigger than `limit`. <p> `Some(result)` if amount of distinct values is within `limit`.
    */
  def countOccurrencesWithKeyLimit[T](rdd: RDD[T], limit: Long): Option[Map[T, Long]] =
    CountOccurrencesWithKeyLimitAggregator(limit).execute(rdd)

  /** Returns Spark's DataFrame column name safe for using in SQL expressions.
    * @param columnName
    * @return
    *   properly escaped column name
    */
  def escapeColumnName(columnName: String) =
    // We had to forbid backticks in column names due to anomalies in Spark 1.6
    // See: https://issues.apache.org/jira/browse/SPARK-13297
    // "`" + columnName.replace("`", "``") + "`"
    "`" + columnName + "`"

}
