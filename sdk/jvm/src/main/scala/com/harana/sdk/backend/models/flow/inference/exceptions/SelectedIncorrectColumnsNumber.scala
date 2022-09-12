package com.harana.sdk.backend.models.flow.inference.exceptions

import com.harana.sdk.shared.models.flow.exceptions.FlowError
import com.harana.sdk.shared.models.flow.parameters.selections.MultipleColumnSelection

case class SelectedIncorrectColumnsNumber(multipleColumnSelection: MultipleColumnSelection,
                                          selectedColumnsNames: Seq[String],
                                          modelsCount: Int) extends FlowError {

  val message = s"The selection '$multipleColumnSelection' selects ${selectedColumnsNames.size} column(s): ${selectedColumnsNames.mkString(", ")}. Expected to select $modelsCount column(s)."
}