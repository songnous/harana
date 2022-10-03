package com.harana.sdk.backend.models.flow.actiontypes.examples

import java.io.File
import java.io.PrintWriter
import com.harana.sdk.shared.models.flow.actiontypes.inout.CsvParameters.ColumnSeparatorChoice.Comma
import com.harana.sdk.backend.models.flow.IntegratedTestSupport
import com.harana.sdk.backend.models.flow.actionobjects.dataframe.DataFrame
import com.harana.sdk.backend.models.flow.actiontypes.ActionType
import com.harana.sdk.backend.models.flow.actiontypes.read.ReadDataFrame
import com.harana.sdk.backend.models.flow.actiontypes.readwritedataframe.FileScheme
import com.harana.sdk.backend.models.flow.utils.Logging
import com.harana.sdk.shared.models.flow.actionobjects.ActionObjectInfo

abstract class AbstractActionExample[T <: ActionType] extends IntegratedTestSupport with Logging {

  def action: T

  final def className = action.getClass.getSimpleName

  def fileNames = Seq.empty[String]

  def loadCsv(fileName: String) = {
    ReadDataFrame(
      FileScheme.File.pathPrefix + this.getClass.getResource(s"/test_files/$fileName.csv").getPath,
      Comma(),
      csvNamesIncluded = true,
      csvConvertToBoolean = false
    ).executeUntyped(List.empty[ActionObjectInfo])(executionContext).head.asInstanceOf[DataFrame]
  }

  def inputDataFrames = fileNames.map(loadCsv)

  className should {
    "successfully run execute() and generate example" in {
      val op = action
      val outputDfs = op
        .executeUntyped(inputDataFrames.toList)(executionContext)
        .collect { case df: DataFrame => df }
      val html = ExampleHtmlFormatter.exampleHtml(op, inputDataFrames, outputDfs)

      // TODO Make it not rely on relative path it's run from
      val examplePageFile = new File("docs/actions/examples/" + className + ".md")

      examplePageFile.getParentFile.mkdirs()
      examplePageFile.createNewFile()

      val writer = new PrintWriter(examplePageFile)
      writer.println(html)
      writer.flush()
      writer.close()
      logger.info("Created doc page for " + className)
    }
  }
}
