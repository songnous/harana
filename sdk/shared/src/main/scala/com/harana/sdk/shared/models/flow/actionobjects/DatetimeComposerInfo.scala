package com.harana.sdk.shared.models.flow.actionobjects

import com.harana.sdk.shared.models.flow.parameters.choice.{Choice, MultipleChoiceParameter}
import com.harana.sdk.shared.models.flow.parameters.selections.SingleColumnSelection
import com.harana.sdk.shared.models.flow.parameters.{ParameterGroup, SingleColumnCreatorParameter, SingleColumnSelectorParameter}
import io.circe.generic.JsonCodec

trait DatetimeComposerInfo extends TransformerInfo {

  import DatetimeComposerInfo._

  val id = "DB26B2A0-658F-41D2-A8D1-10A17654B284"

  val timestampColumnsParameter = MultipleChoiceParameter[TimestampPartColumnChoice]("parts", default = Some(Set.empty[TimestampPartColumnChoice]))
  def timestampColumns = $(timestampColumnsParameter)
  def setTimestampColumns(timestampParts: Set[TimestampPartColumnChoice]): this.type = set(timestampColumnsParameter, timestampParts)

  val outputColumnParameter = SingleColumnCreatorParameter("output-column", default = Some("Timestamp"))
  def outputColumn = $(outputColumnParameter)
  def setOutputColumn(outputColumn: String): this.type = set(outputColumnParameter, outputColumn)

  override val parameterGroups = List(ParameterGroup("", timestampColumnsParameter, outputColumnParameter))
}

object DatetimeComposerInfo extends DatetimeComposerInfo {

  import TimestampPartColumnChoice._

  val orderedTimestampParts = List[TimestampPartColumnChoice](Year(), Month(), Day(), Hour(), Minutes(), Seconds())
  val choiceOrder = orderedTimestampParts.map(_.getClass)

  @JsonCodec
  sealed trait TimestampPartColumnChoice extends Choice {
    val name: String
    val defaultValue: Int
    val formatString: String

    val timestampColumnSelectorParameter = SingleColumnSelectorParameter(name + " column", portIndex = 0)
    def timestampColumn = $(timestampColumnSelectorParameter)
    def setTimestampColumn(timestampColumn: SingleColumnSelection): this.type = set(timestampColumnSelectorParameter, timestampColumn)

    val choiceOrder = DatetimeComposerInfo.choiceOrder
    override val parameterGroups = List(ParameterGroup("", timestampColumnSelectorParameter))
  }

  object TimestampPartColumnChoice {

    case class Year() extends TimestampPartColumnChoice {
      override val name = "year"
      val defaultValue = 1970
      val formatString = "%04.0f"
    }

    case class Month() extends TimestampPartColumnChoice {
      override val name = "month"
      val defaultValue = 1
      val formatString = "%02.0f"
    }

    case class Day() extends TimestampPartColumnChoice {
      override val name = "day"
      val defaultValue = 1
      val formatString = "%02.0f"
    }

    case class Hour() extends TimestampPartColumnChoice {
      override val name = "hour"
      val defaultValue = 0
      val formatString = "%02.0f"
    }

    case class Minutes() extends TimestampPartColumnChoice {
      override val name = "minutes"
      val defaultValue = 0
      val formatString = "%02.0f"
    }

    case class Seconds() extends TimestampPartColumnChoice {
      override val name = "seconds"
      val defaultValue = 0
      val formatString = "%02.0f"
    }
  }
}
