package com.harana.sdk.shared.models.flow.actiontypes.inout

import com.harana.sdk.shared.models.flow.parameters.choice.Choice.ChoiceOption
import com.harana.sdk.shared.models.flow.parameters.{FileFormat, Parameter, ParameterGroup}
import com.harana.sdk.shared.models.flow.parameters.choice.Choice

sealed trait InputFileFormatChoice extends Choice {
  val choiceOrder = InputFileFormatChoice.choiceOrder
}

object InputFileFormatChoice {

  class Csv() extends InputFileFormatChoice with CsvParameters with HasShouldConvertToBooleanParameter {
    val name = FileFormat.CSV.toString.toLowerCase
    override val parameterGroups = List(ParameterGroup("", csvColumnSeparatorParameter, namesIncludedParameter, shouldConvertToBooleanParameter))
  }

  class Parquet() extends InputFileFormatChoice {
    val name = FileFormat.PARQUET.toString.toLowerCase
    override val parameterGroups = List.empty[ParameterGroup]
  }

  class Json() extends InputFileFormatChoice {
    val name = FileFormat.JSON.toString.toLowerCase
    override val parameterGroups = List.empty[ParameterGroup]
  }

  val choiceOrder: List[ChoiceOption] = List(
    classOf[Csv],
    classOf[Parquet],
    classOf[Json]
  )
}