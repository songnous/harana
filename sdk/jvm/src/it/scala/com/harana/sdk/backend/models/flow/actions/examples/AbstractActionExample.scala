package com.harana.sdk.backend.models.flow.actions.examples

import java.io.File
import java.io.PrintWriter
import com.harana.sdk.shared.models.flow.actions.inout.CsvParameters.ColumnSeparatorChoice.Comma
import com.harana.sdk.backend.models.flow.{Action, IntegratedTestSupport}
import com.harana.sdk.backend.models.flow.actionobjects.dataframe.DataFrame
import com.harana.sdk.backend.models.flow.actions.read.ReadDataFrame
import com.harana.sdk.backend.models.flow.actions.readwritedataframe.FileScheme
import com.harana.sdk.backend.models.flow.utils.Logging
import com.harana.sdk.shared.models.flow.ActionObjectInfo

abstract class AbstractActionExample[T <: Action] extends IntegratedTestSupport with Logging {

  def action: T

  final def className = action.getClass.getSimpleName

  def fileNames = Seq.empty[String]

  def loadCsv(fileName: String) = {
    ReadDataFrame(
      FileScheme.File.pathPrefix + this.getClass.getResource(s"/test_files/$fileName.csv").getPath,
      Comma(),
      csvNamesIncluded = true,
      csvConvertToBoolean = false
    ).executeUntyped(Vector.empty[ActionObjectInfo])(executionContext).head.asInstanceOf[DataFrame]
  }

  def inputDataFrames = fileNames.map(loadCsv)

  className should {
    "successfully run execute() and generate example" in {
      val op = action
      val outputDfs = op
        .executeUntyped(inputDataFrames.toVector)(executionContext)
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
