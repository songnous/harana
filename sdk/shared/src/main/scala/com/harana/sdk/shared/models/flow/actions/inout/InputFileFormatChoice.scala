package com.harana.sdk.shared.models.flow.actions.inout

import com.harana.sdk.shared.models.flow.parameters.choice.Choice.ChoiceOption
import com.harana.sdk.shared.models.flow.parameters.{FileFormat, Parameter}
import com.harana.sdk.shared.models.flow.parameters.choice.Choice

sealed trait InputFileFormatChoice extends Choice {
  val choiceOrder = InputFileFormatChoice.choiceOrder
}

object InputFileFormatChoice {

  class Csv() extends InputFileFormatChoice with CsvParameters with HasShouldConvertToBooleanParameter {
    val name = FileFormat.CSV.toString
    val parameters = Left(List(csvColumnSeparatorParameter, namesIncludedParameter, shouldConvertToBooleanParameter))
  }

  class Parquet() extends InputFileFormatChoice {
    val name = FileFormat.PARQUET.toString
    val parameters = Left(List.empty[Parameter[_]])
  }

  class Json() extends InputFileFormatChoice {
    val name = FileFormat.JSON.toString
    val parameters = Left(List.empty[Parameter[_]])
  }

  val choiceOrder: List[ChoiceOption] = List(
    classOf[Csv],
    classOf[Parquet],
    classOf[Json]
  )
}