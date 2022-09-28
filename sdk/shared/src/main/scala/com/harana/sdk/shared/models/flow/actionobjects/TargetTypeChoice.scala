package com.harana.sdk.shared.models.flow.actionobjects

import com.harana.sdk.shared.models.flow.DataType._
import com.harana.sdk.shared.models.designer.flow._
import com.harana.sdk.shared.models.flow.parameters.choice.Choice.ChoiceOption
import com.harana.sdk.shared.models.flow.DataType
import com.harana.sdk.shared.models.flow.DataType.{BooleanType, DoubleType, FloatType, IntegerType, LongType, StringType, TimestampType}
import com.harana.sdk.shared.models.flow.parameters.{Parameter, ParameterGroup}
import com.harana.sdk.shared.models.flow.parameters.choice.Choice

sealed abstract class TargetTypeChoice(val columnType: DataType) extends Choice {
  val choiceOrder = TargetTypeChoices.choiceOrder
  val parameterGroups = List.empty[ParameterGroup]
  val name = columnType.getClass.getSimpleName
}

object TargetTypeChoices {
  val choiceOrder: List[ChoiceOption] = List(
    StringTargetTypeChoice(),
    BooleanTargetTypeChoice(),
    TimestampTargetTypeChoice(),
    DoubleTargetTypeChoice(),
    FloatTargetTypeChoice(),
    LongTargetTypeChoice(),
    IntegerTargetTypeChoice()
  ).map(_.getClass)

  case class StringTargetTypeChoice() extends TargetTypeChoice(StringType)
  case class DoubleTargetTypeChoice() extends TargetTypeChoice(DoubleType)
  case class TimestampTargetTypeChoice() extends TargetTypeChoice(TimestampType)
  case class BooleanTargetTypeChoice() extends TargetTypeChoice(BooleanType)
  case class IntegerTargetTypeChoice() extends TargetTypeChoice(IntegerType)
  case class FloatTargetTypeChoice() extends TargetTypeChoice(FloatType)
  case class LongTargetTypeChoice() extends TargetTypeChoice(LongType)
}