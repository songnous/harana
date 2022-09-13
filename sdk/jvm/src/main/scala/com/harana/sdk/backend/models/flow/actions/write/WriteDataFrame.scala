package com.harana.sdk.backend.models.flow.actions.write

import com.harana.sdk.backend.models.flow._
import com.harana.sdk.backend.models.flow.actionobjects.dataframe.DataFrame
import com.harana.sdk.backend.models.flow.actions.exceptions.HaranaIOError
import com.harana.sdk.backend.models.flow.actions.readwritedataframe.filestorage.DataFrameToFileWriter
import com.harana.sdk.backend.models.flow.actions.readwritedataframe.googlestorage.DataFrameToGoogleSheetWriter
import com.harana.sdk.backend.models.flow.actions.readwritedataframe.validators.{FilePathHasValidFileScheme, ParquetSupportedOnClusterOnly}
import com.harana.sdk.backend.models.flow.inference.InferContext
import com.harana.sdk.backend.models.flow.actions.readwritedataframe.filestorage.DataFrameToFileWriter
import com.harana.sdk.backend.models.flow.actions.readwritedataframe.googlestorage.DataFrameToGoogleSheetWriter
import com.harana.sdk.backend.models.flow.actions.readwritedataframe.validators.{FilePathHasValidFileScheme, ParquetSupportedOnClusterOnly}
import com.harana.sdk.shared.models.flow.actions.inout.OutputStorageTypeChoice
import com.harana.sdk.shared.models.flow.actions.write.WriteDataFrameInfo
import org.apache.spark.sql.SaveMode

import java.io.IOException
import java.util.Properties

class WriteDataFrame extends Action1To0[DataFrame] with WriteDataFrameInfo {

  def execute(df: DataFrame)(context: ExecutionContext) = {
    import OutputStorageTypeChoice._
    try {
      getStorageType match {
        case jdbcChoice: Jdbc => writeToJdbc(jdbcChoice, context, df)
        case googleSheetChoice: GoogleSheet => DataFrameToGoogleSheetWriter.writeToGoogleSheet(googleSheetChoice, context, df)
        case fileChoice: File => DataFrameToFileWriter.writeToFile(fileChoice, context, df)
      }
    } catch {
      case e: IOException =>
        println(s"WriteDataFrame error. Could not write file to designated storage", e)
        throw HaranaIOError(e).toException
    }
  }

  private def writeToJdbc(jdbcChoice: OutputStorageTypeChoice.Jdbc, context: ExecutionContext, df: DataFrame) = {
    val properties = new Properties()
    properties.setProperty("driver", jdbcChoice.getJdbcDriverClassName)

    val jdbcUrl = jdbcChoice.getJdbcUrl
    val jdbcTableName = jdbcChoice.getJdbcTableName
    val saveMode = if (jdbcChoice.getShouldOverwrite) SaveMode.Overwrite else SaveMode.ErrorIfExists

    df.sparkDataFrame.write.mode(saveMode).jdbc(jdbcUrl, jdbcTableName, properties)
  }

  override def inferKnowledge(k0: Knowledge[DataFrame])(context: InferContext) = {
    FilePathHasValidFileScheme.validate(this)
    ParquetSupportedOnClusterOnly.validate(this)
    super.inferKnowledge(k0)(context)
  }
}