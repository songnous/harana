package com.harana.sdk.backend.models.flow.actiontypes.read

import com.harana.sdk.backend.models.flow.ExecutionContext
import com.harana.sdk.backend.models.flow.actionobjects.dataframe.DataFrame
import com.harana.sdk.backend.models.flow.actiontypes.ActionTypeType0To1
import com.harana.sdk.backend.models.flow.actiontypes.exceptions.{HaranaIOError, HaranaUnknownHostError}
import com.harana.sdk.backend.models.flow.actiontypes.readwritedataframe.filestorage.DataFrameFromFileReader
import com.harana.sdk.backend.models.flow.actiontypes.readwritedataframe.googlestorage.DataFrameFromGoogleSheetReader
import com.harana.sdk.shared.models.flow.actiontypes.inout.InputStorageTypeChoice
import com.harana.sdk.shared.models.flow.actiontypes.read.ReadDataFrameInfo
import org.apache.spark.sql

import scala.reflect.runtime.universe.TypeTag
import java.io._
import java.net.UnknownHostException

class ReadDataFrame extends ActionTypeType0To1[DataFrame] with ReadDataFrameInfo {

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