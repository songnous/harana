package com.harana.sdk.backend.models.flow.actions.read

import com.harana.sdk.backend.models.flow.{Action0To1, ExecutionContext}
import com.harana.sdk.backend.models.flow.actionobjects.dataframe.DataFrame
import com.harana.sdk.backend.models.flow.actions.exceptions.{HaranaIOError, HaranaUnknownHostError}
import com.harana.sdk.backend.models.flow.actions.readwritedataframe.filestorage.DataFrameFromFileReader
import com.harana.sdk.backend.models.flow.actions.readwritedataframe.googlestorage.DataFrameFromGoogleSheetReader
import com.harana.sdk.shared.models.flow.actions.inout.InputStorageTypeChoice
import com.harana.sdk.shared.models.flow.actions.read.ReadDataFrameInfo
import org.apache.spark.sql

import scala.reflect.runtime.universe.TypeTag
import java.io._
import java.net.UnknownHostException

class ReadDataFrame extends Action0To1[DataFrame] with ReadDataFrameInfo {

  def execute()(context: ExecutionContext) = {
    implicit val ec = context

    try {
      val dataframe = getStorageType match {
        case jdbcChoice: InputStorageTypeChoice.Jdbc => readFromJdbc(jdbcChoice)
        case googleSheet: InputStorageTypeChoice.GoogleSheet => DataFrameFromGoogleSheetReader.readFromGoogleSheet(googleSheet)
        case fileChoice: InputStorageTypeChoice.File => DataFrameFromFileReader.readFromFile(fileChoice)
      }
      DataFrame.fromSparkDataFrame(dataframe)
    } catch {
      case e: UnknownHostException => throw HaranaUnknownHostError(e).toException
      case e: IOException          => throw HaranaIOError(e).toException
    }
  }

  private def readFromJdbc(jdbcChoice: InputStorageTypeChoice.Jdbc)(implicit context: ExecutionContext): sql.DataFrame =
    context.sparkSQLSession
      .read.format("jdbc")
      .option("driver", jdbcChoice.getJdbcDriverClassName)
      .option("url", jdbcChoice.getJdbcUrl)
      .option("dbtable", jdbcChoice.getJdbcTableName)
      .load()

  lazy val tTagTO_0: TypeTag[DataFrame] = typeTag

}