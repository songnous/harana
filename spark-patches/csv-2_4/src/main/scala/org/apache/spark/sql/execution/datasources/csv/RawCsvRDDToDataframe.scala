package org.apache.spark.sql.execution.datasources.csv

import com.univocity.parsers.csv.CsvParser

import scala.util.Try
import org.apache.spark.rdd.RDD
import org.apache.spark.sql._
import org.apache.spark.sql.catalyst.csv.{CSVOptions, UnivocityParser}
import org.apache.spark.sql.execution.LogicalRDD
import org.apache.spark.sql.types.{StructType, _}

object RawCsvRDDToDataframe {

  def parse(rdd: RDD[String], sparkSession: SparkSession, options: Map[String, String]) = {
    val csvOptions = new CSVOptions(options, true, sparkSession.sessionState.conf.sessionLocalTimeZone)
    val csvReader = new CsvParser(csvOptions.asParserSettings)
    val firstLine = findFirstLine(csvOptions, rdd)
    val firstRow = csvReader.parseLine(firstLine)
    val header = if (csvOptions.headerFlag)
      firstRow.zipWithIndex.map { case (value, index) =>
        if (value == null || value.isEmpty || value == csvOptions.nullValue) s"_c$index" else value
      }
    else
      firstRow.zipWithIndex.map { case (value, index) => s"_c$index" }

    // TODO Migrate to Spark's schema inferencer eventually
    // val schema = CSVInferSchema.infer(parsedRdd, header, csvOptions)
    val schema = {
      val schemaFields = header.map { h => StructField(h, StringType, nullable = true) }
      StructType(schemaFields)
    }

    val withoutHeader =
      if (csvOptions.headerFlag)
        rdd.zipWithIndex().filter { case (_, index) => index != 0 }.map { case (row, _) => row }
      else rdd

    val univocityParser = new UnivocityParser(schema, csvOptions)
    val internalRows = withoutHeader.filter(row => row.trim.nonEmpty).flatMap { row =>
      Try(univocityParser.parse(row)).get
    }

    Dataset.ofRows(sparkSession, LogicalRDD(schema.toAttributes, internalRows)(sparkSession))
  }

  private def findFirstLine(options: CSVOptions, rdd: RDD[String]) =
    if (options.isCommentSet) {
      val comment = options.comment.toString
      rdd.filter(l => l.trim.nonEmpty && !l.startsWith(comment)).first()
    } else
      rdd.filter(_.trim.nonEmpty).first()
}
