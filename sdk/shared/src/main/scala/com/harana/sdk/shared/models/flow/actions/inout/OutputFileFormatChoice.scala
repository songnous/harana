package com.harana.sdk.shared.models.flow.actions.inout

import com.harana.sdk.shared.models.flow.parameters.choice.Choice.ChoiceOption
import com.harana.sdk.shared.models.flow.parameters.{FileFormat, Parameter}
import com.harana.sdk.shared.models.flow.parameters.choice.Choice

sealed trait OutputFileFormatChoice extends Choice {
  import OutputFileFormatChoice._
  val choiceOrder: List[ChoiceOption] = List(classOf[Csv], classOf[Parquet], classOf[Json])
}

object OutputFileFormatChoice {

  class Csv() extends OutputFileFormatChoice with CsvParameters {
    val name = FileFormat.CSV.toString
    val parameters = Left(Array(csvColumnSeparatorParameter, namesIncludedParameter))
  }

  class Parquet() extends OutputFileFormatChoice {
    val name = FileFormat.PARQUET.toString
    val parameters = Left(Array.empty[Parameter[_]])
  }

  class Json() extends OutputFileFormatChoice {
    val name = FileFormat.JSON.toString
    val parameters = Left(Array.empty[Parameter[_]])
  }
}

object OutputFromInputFileFormat {

  def apply(inputFileFormatChoice: InputFileFormatChoice): OutputFileFormatChoice =
    inputFileFormatChoice match {
      case csv: InputFileFormatChoice.Csv         => csv.copyValues(new OutputFileFormatChoice.Csv())
      case json: InputFileFormatChoice.Json       => new OutputFileFormatChoice.Json()
      case parquet: InputFileFormatChoice.Parquet => new OutputFileFormatChoice.Parquet()
      case unsupported                            => throw new IllegalStateException(s"Unsupported input file format $inputFileFormatChoice")
    }
}
