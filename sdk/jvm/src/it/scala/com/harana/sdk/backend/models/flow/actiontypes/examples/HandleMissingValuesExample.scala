package com.harana.sdk.backend.models.flow.actiontypes.examples

import com.harana.sdk.shared.models.designer.flow.actionobjects.MissingValuesHandler.MissingValueIndicatorChoice.No
import com.harana.sdk.shared.models.designer.flow.actionobjects.MissingValuesHandler.Strategy.RemoveRow
import com.harana.sdk.backend.models.flow.actiontypes.HandleMissingValues
import com.harana.sdk.shared.models.flow.parameters.selections.{MultipleColumnSelection, NameColumnSelection}

class HandleMissingValuesExample extends AbstractActionExample[HandleMissingValues] {

  def action: HandleMissingValues = {
    val op = new HandleMissingValues()
    op.transformer
      .setUserDefinedMissingValues(Seq("-1.0"))
      .setSelectedColumns(MultipleColumnSelection(List(NameColumnSelection(Set("baths", "price")))))
      .setStrategy(RemoveRow())
      .setMissingValueIndicator(No())
    op.set(op.transformer.extractParameterMap())
  }

  override def fileNames = Seq("example_missing_values")

}
