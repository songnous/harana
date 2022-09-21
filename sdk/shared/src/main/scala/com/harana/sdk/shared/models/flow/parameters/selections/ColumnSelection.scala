package com.harana.sdk.shared.models.flow.parameters.selections

import com.harana.sdk.shared.models.flow.exceptions.FlowError
import com.harana.sdk.shared.models.flow.parameters.exceptions.IllegalIndexRangeColumnSelectionError
import com.harana.sdk.shared.models.flow.utils.ColumnType
import io.circe.generic.JsonCodec

@SerialVersionUID(1)
@JsonCodec
sealed trait ColumnSelection {
  val typeName: String
  def validate = List.empty[FlowError]
}

case class NameColumnSelection(names: Set[String]) extends ColumnSelection {
  val typeName = NameColumnSelection.typeName
}
object NameColumnSelection {
  val typeName = "columnList"
}

case class IndexColumnSelection(indexes: Set[Int]) extends ColumnSelection {
  val typeName = IndexColumnSelection.typeName
}
object IndexColumnSelection {
  val typeName = "indexList"
}

case class TypeColumnSelection(types: Set[ColumnType]) extends ColumnSelection {
  val typeName = TypeColumnSelection.typeName
}
object TypeColumnSelection {
  val typeName = "typeList"
}

case class IndexRangeColumnSelection(lowerBound: Option[Int], upperBound: Option[Int]) extends ColumnSelection {
  val typeName = IndexRangeColumnSelection.typeName
  override def validate = {
    val lowerLessThanUpper = for {
      lower <- lowerBound
      upper <- upperBound
    } yield lower <= upper
    val valid = lowerLessThanUpper.getOrElse(false)
    if (valid) List.empty[FlowError] else List(IllegalIndexRangeColumnSelectionError(this))
  }
}

object IndexRangeColumnSelection {
  val typeName = "indexRange"
}

@SerialVersionUID(1)
@JsonCodec
sealed trait SingleColumnSelection {
  val typeName: String
}

case class IndexSingleColumnSelection(value: Int) extends SingleColumnSelection {
  val typeName = IndexSingleColumnSelection.typeName
}
object IndexSingleColumnSelection {
  val typeName = "index"
}

case class NameSingleColumnSelection(value: String) extends SingleColumnSelection {
  val typeName = NameSingleColumnSelection.typeName
}
object NameSingleColumnSelection {
  val typeName = "column"
}

case class MultipleColumnSelection(selections: List[ColumnSelection], excluding: Boolean = false) {
  def validate = selections.flatMap(selection => selection.validate)
}

object MultipleColumnSelection {
  val emptySelection = new MultipleColumnSelection(List.empty)
  val selectionsField = "selections"
  val excludingField = "excluding"
}