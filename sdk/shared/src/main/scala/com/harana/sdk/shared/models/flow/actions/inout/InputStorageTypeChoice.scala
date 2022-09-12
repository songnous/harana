package com.harana.sdk.shared.models.flow.actions.inout

import com.harana.sdk.shared.models.flow.parameters.choice.Choice.ChoiceOption
import com.harana.sdk.shared.models.flow.parameters.StorageType
import com.harana.sdk.shared.models.flow.parameters.choice.{Choice, ChoiceParameter}
import com.harana.sdk.shared.models.flow.parameters.library.LoadFromLibraryParameter

sealed trait InputStorageTypeChoice extends Choice {
  import InputStorageTypeChoice._

  val choiceOrder: List[ChoiceOption] = List(classOf[File], classOf[Jdbc], classOf[GoogleSheet])
}

object InputStorageTypeChoice {

  class File extends InputStorageTypeChoice {
    val name = StorageType.File.toString

    val sourceFileParameter = LoadFromLibraryParameter("source", Some("Path to the DataFrame file."))
    def getSourceFile = $(sourceFileParameter)
    def setSourceFile(value: String): this.type = set(sourceFileParameter, value)

    val fileFormatParameter = ChoiceParameter[InputFileFormatChoice]("format", Some("Format of the input file."))
    setDefault(fileFormatParameter, new InputFileFormatChoice.Csv())
    def getFileFormat = $(fileFormatParameter)
    def setFileFormat(value: InputFileFormatChoice): this.type = set(fileFormatParameter, value)

    val parameters = Array(sourceFileParameter, fileFormatParameter)
  }

  class Jdbc extends InputStorageTypeChoice with JdbcParameters {
    val name = StorageType.Jdbc.toString

    val parameters = Array(jdbcUrlParameter, jdbcDriverClassNameParameter, jdbcTableNameParameter)
  }

  class GoogleSheet extends InputStorageTypeChoice with GoogleSheetParameters with NamesIncludedParameter with HasShouldConvertToBooleanParameter {
    val name = "Google Sheet"

    val parameters = Array(
      googleSheetIdParameter,
      serviceAccountCredentialsParameter,
      namesIncludedParameter,
      shouldConvertToBooleanParameter
    )
  }
}