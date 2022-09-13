package com.harana.sdk.shared.models.flow.actions.inout

import com.harana.sdk.shared.models.flow.parameters.choice.Choice.ChoiceOption
import com.harana.sdk.shared.models.flow.parameters.{BooleanParameter, StorageType}
import com.harana.sdk.shared.models.flow.parameters.choice.{Choice, ChoiceParameter}
import com.harana.sdk.shared.models.flow.parameters.library.SaveToLibraryParameter

sealed trait OutputStorageTypeChoice extends Choice {
  import OutputStorageTypeChoice._
  val choiceOrder: List[ChoiceOption] = List(classOf[File], classOf[Jdbc], classOf[GoogleSheet])
}

object OutputStorageTypeChoice {

  class File() extends OutputStorageTypeChoice {
    val name = StorageType.File.toString
    val outputFileParameter = SaveToLibraryParameter("output file", Some("Output file path."))
    def getOutputFile = $(outputFileParameter)
    def setOutputFile(value: String): this.type = set(outputFileParameter, value)

    val shouldOverwriteParameter = BooleanParameter("overwrite", Some("Should saving a file overwrite an existing file with the same name?"))
    setDefault(shouldOverwriteParameter, true)
    def getShouldOverwrite = $(shouldOverwriteParameter)
    def setShouldOverwrite(value: Boolean): this.type = set(shouldOverwriteParameter, value)

    val fileFormatParameter = ChoiceParameter[OutputFileFormatChoice]("format", Some("Format of the output file."))
    setDefault(fileFormatParameter, new OutputFileFormatChoice.Csv())

    def getFileFormat = $(fileFormatParameter)
    def setFileFormat(value: OutputFileFormatChoice): this.type = set(fileFormatParameter, value)
    val parameters = Array(outputFileParameter, shouldOverwriteParameter, fileFormatParameter)
  }

  class Jdbc() extends OutputStorageTypeChoice with JdbcParameters {

    val name = StorageType.Jdbc.toString

    val shouldOverwriteParameter = BooleanParameter("overwrite", Some("Should saving a table overwrite an existing table with the same name?"))
    setDefault(shouldOverwriteParameter, true)
    def getShouldOverwrite = $(shouldOverwriteParameter)
    def setShouldOverwrite(value: Boolean): this.type = set(shouldOverwriteParameter, value)

    val parameters = Array(jdbcUrlParameter, jdbcDriverClassNameParameter, jdbcTableNameParameter, shouldOverwriteParameter)
  }

  class GoogleSheet() extends OutputStorageTypeChoice with GoogleSheetParameters with NamesIncludedParameter with HasShouldConvertToBooleanParameter {

    val name = "Google Sheet"

    override val parameters = Array(
      googleSheetIdParameter,
      serviceAccountCredentialsParameter,
      namesIncludedParameter,
      shouldConvertToBooleanParameter
    )
  }
}