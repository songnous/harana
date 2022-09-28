package com.harana.sdk.shared.models.flow.actions.inout

import com.harana.sdk.shared.models.flow.parameters.choice.Choice.ChoiceOption
import com.harana.sdk.shared.models.flow.parameters.{FileFormat, Parameter, ParameterGroup}
import com.harana.sdk.shared.models.flow.parameters.choice.Choice

sealed trait InputFileFormatChoice extends Choice {
  val choiceOrder = InputFileFormatChoice.choiceOrder
}

object InputFileFormatChoice {

  class Csv() extends InputFileFormatChoice with CsvParameters with HasShouldConvertToBooleanParameter {
    val name = FileFormat.CSV.toString
    val parameterGroups = List(ParameterGroup(None, csvColumnSeparatorParameter, namesIncludedParameter, shouldConvertToBooleanParameter))
  }

  class Parquet() extends InputFileFormatChoice {
    val name = FileFormat.PARQUET.toString
    val parameterGroups = List.empty[ParameterGroup]
  }

  class Json() extends InputFileFormatChoice {
    val name = FileFormat.JSON.toString
    val parameterGroups = List.empty[ParameterGroup]
  }

  val choiceOrder: List[ChoiceOption] = List(
    classOf[Csv],
    classOf[Parquet],
    classOf[Json]
  )
}