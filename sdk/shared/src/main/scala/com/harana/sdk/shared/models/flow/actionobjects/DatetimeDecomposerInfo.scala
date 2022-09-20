package com.harana.sdk.shared.models.flow.actionobjects

import com.harana.sdk.shared.models.flow.parameters.choice.Choice.ChoiceOption
import com.harana.sdk.shared.models.flow.parameters.choice.{Choice, MultipleChoiceParameter}
import com.harana.sdk.shared.models.flow.parameters.{Parameter, PrefixBasedColumnCreatorParameter, SingleColumnSelectorParameter}
import com.harana.sdk.shared.models.flow.parameters.selections.SingleColumnSelection

trait DatetimeDecomposerInfo extends TransformerInfo {

  import DatetimeDecomposerInfo._

  val id = "D9FE1921-2960-4CCF-BDAF-B73B56BD46B4"

  val timestampColumnParameter = SingleColumnSelectorParameter("timestamp column", portIndex = 0)
  def timestampColumn = $(timestampColumnParameter)
  def setTimestampColumn(timestampColumn: SingleColumnSelection): this.type = set(timestampColumnParameter, timestampColumn)

  val timestampPartsParameter = MultipleChoiceParameter[TimestampPart]("parts")
  def getTimestampParts = $(timestampPartsParameter)
  def setTimestampParts(timestampParts: Set[TimestampPart]): this.type = set(timestampPartsParameter, timestampParts)

  val timestampPrefixParameter = PrefixBasedColumnCreatorParameter("prefix")
  setDefault(timestampPrefixParameter, "")
  def getTimestampPrefix = $(timestampPrefixParameter)
  def setTimestampPrefix(timestampPrefix: String): this.type = set(timestampPrefixParameter, timestampPrefix)

  val parameters = Array(
    timestampColumnParameter,
    timestampPartsParameter,
    timestampPrefixParameter
  )
}

object DatetimeDecomposerInfo extends DatetimeDecomposerInfo {

  import TimestampPart._

  sealed trait TimestampPart extends Choice {
    val choiceOrder: List[ChoiceOption] = List(classOf[Year], classOf[Month], classOf[Day], classOf[Hour], classOf[Minutes], classOf[Seconds])
  }

  object TimestampPart {

    case class Year() extends TimestampPart {
      val name = "year"
      val parameters = Array.empty[Parameter[_]]
    }

    case class Month() extends TimestampPart {
      val name = "month"
      val parameters = Array.empty[Parameter[_]]
    }

    case class Day() extends TimestampPart {
      val name = "day"
      val parameters = Array.empty[Parameter[_]]
    }

    case class Hour() extends TimestampPart {
      val name = "hour"
      val parameters = Array.empty[Parameter[_]]
    }

    case class Minutes() extends TimestampPart {
      val name = "minutes"
      val parameters = Array.empty[Parameter[_]]
    }

    case class Seconds() extends TimestampPart {
      val name = "seconds"
      val parameters = Array.empty[Parameter[_]]
    }
  }

  case class TimestampPartRange(part: TimestampPart, start: Int, length: Int)

  val timestampPartRanges = List(
    TimestampPartRange(Year(), 0, 4),
    TimestampPartRange(Month(), 6, 2),
    TimestampPartRange(Day(), 9, 2),
    TimestampPartRange(Hour(), 12, 2),
    TimestampPartRange(Minutes(), 15, 2),
    TimestampPartRange(Seconds(), 18, 2)
  )
}