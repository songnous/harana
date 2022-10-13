package com.harana.sdk.shared.models.flow.actiontypes.inout

import com.harana.sdk.shared.models.flow.parameters.choice.Choice.ChoiceOption
import com.harana.sdk.shared.models.flow.parameters.{ParameterGroup, StorageType}
import com.harana.sdk.shared.models.flow.parameters.choice.{Choice, ChoiceParameter}
import com.harana.sdk.shared.models.flow.parameters.library.LoadFromLibraryParameter

sealed trait InputStorageTypeChoice extends Choice {
  import InputStorageTypeChoice._

  val choiceOrder: List[ChoiceOption] = List(classOf[File], classOf[Jdbc], classOf[GoogleSheet])
}

object InputStorageTypeChoice {

  class File extends InputStorageTypeChoice {
    val name = StorageType.File.toString.toLowerCase

    val sourceFileParameter = LoadFromLibraryParameter("source")
    def getSourceFile = $(sourceFileParameter)
    def setSourceFile(value: String): this.type = set(sourceFileParameter, value)

    val fileFormatParameter = ChoiceParameter[InputFileFormatChoice]("format", default = Some(new InputFileFormatChoice.Csv()))
    def getFileFormat = $(fileFormatParameter)
    def setFileFormat(value: InputFileFormatChoice): this.type = set(fileFormatParameter, value)

    override val parameterGroups = List(ParameterGroup("", sourceFileParameter, fileFormatParameter))
  }

  class Jdbc extends InputStorageTypeChoice with JdbcParameters {
    val name = StorageType.Jdbc.toString.toLowerCase

    override val parameterGroups = List(ParameterGroup("", jdbcUrlParameter, jdbcDriverClassNameParameter, jdbcTableNameParameter))
  }

  class GoogleSheet extends InputStorageTypeChoice with GoogleSheetParameters with NamesIncludedParameter with HasShouldConvertToBooleanParameter {
    val name = "google-sheet"

    override val parameterGroups = List(ParameterGroup("",
      googleSheetIdParameter,
      serviceAccountCredentialsParameter,
      namesIncludedParameter,
      shouldConvertToBooleanParameter
    ))
  }
}