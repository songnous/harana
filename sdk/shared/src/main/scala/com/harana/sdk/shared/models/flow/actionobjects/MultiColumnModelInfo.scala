package com.harana.sdk.shared.models.flow.actionobjects

import com.harana.sdk.shared.models.flow.actionobjects.multicolumn.HasSpecificParameters
import com.harana.sdk.shared.models.flow.actionobjects.multicolumn.MultiColumnParameters.MultiColumnInPlaceChoice
import com.harana.sdk.shared.models.flow.actionobjects.multicolumn.MultiColumnParameters.SingleOrMultiColumnChoices.MultiColumnChoice
import com.harana.sdk.shared.models.flow.parameters.ParameterGroup
import com.harana.sdk.shared.models.flow.parameters.selections.MultipleColumnSelection

trait MultiColumnModelInfo extends SparkModelWrapperInfo with ActionObjectInfo with HasSpecificParameters {

  val multiColumnChoice = MultiColumnChoice()

  override lazy val parameterGroups = {
    val parameters =
      if (specificParameters == null)
        List(multiColumnChoice.inputColumnsParameter, multiColumnChoice.inPlaceChoiceParameter)
      else
        specificParameters.toList ++ List(multiColumnChoice.inputColumnsParameter, multiColumnChoice.inPlaceChoiceParameter)

    List(ParameterGroup("", parameters: _*))
  }

  def setInputColumns(selection: MultipleColumnSelection): this.type = set(multiColumnChoice.inputColumnsParameter -> selection)
  def setInPlaceChoice(choice: MultiColumnInPlaceChoice): this.type = set(multiColumnChoice.inPlaceChoiceParameter -> choice)

}