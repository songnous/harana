package com.harana.sdk.shared.models.flow.parameters.exceptions

import com.harana.sdk.shared.models.flow.parameters.selections.IndexRangeColumnSelection

case class IllegalIndexRangeColumnSelectionError(selection: IndexRangeColumnSelection) extends ValidationError {
  val message = s"The column selection $selection is invalid. All bounds should be set and lower bound should be less or equal upper bound."
}