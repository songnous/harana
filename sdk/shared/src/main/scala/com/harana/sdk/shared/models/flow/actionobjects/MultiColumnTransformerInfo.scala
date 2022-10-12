package com.harana.sdk.shared.models.flow.actionobjects

import com.harana.sdk.shared.models.flow.actionobjects.multicolumn.HasSpecificParameters
import com.harana.sdk.shared.models.flow.actionobjects.multicolumn.MultiColumnParameters.MultiColumnInPlaceChoices.MultiColumnYesInPlace
import com.harana.sdk.shared.models.flow.actionobjects.multicolumn.MultiColumnParameters.SingleOrMultiColumnChoice
import com.harana.sdk.shared.models.flow.actionobjects.multicolumn.MultiColumnParameters.SingleOrMultiColumnChoices.{MultiColumnChoice, SingleColumnChoice}
import com.harana.sdk.shared.models.flow.actionobjects.multicolumn.SingleColumnParameters.SingleTransformInPlaceChoices.NoInPlaceChoice
import com.harana.sdk.shared.models.flow.parameters.selections.{MultipleColumnSelection, NameSingleColumnSelection}
import com.harana.sdk.shared.models.flow.parameters.{IOColumnsParameter, ParameterGroup}

trait MultiColumnTransformerInfo extends TransformerInfo with HasSpecificParameters {

  val singleOrMultiChoiceParameter = IOColumnsParameter(default = Some(SingleColumnChoice()))
  def getSingleOrMultiChoice = $(singleOrMultiChoiceParameter)
  def setSingleOrMultiChoice(value: SingleOrMultiColumnChoice): this.type = set(singleOrMultiChoiceParameter, value)

  override lazy val parameterGroups = {
    val parameters =
      if (specificParameters == null) List(singleOrMultiChoiceParameter)
      else specificParameters.toList :+ singleOrMultiChoiceParameter

    List(ParameterGroup("", parameters: _*))
  }

  def setSingleColumn(inputColumnName: String, outputColumnName: String): this.type = {
    val choice = SingleColumnChoice()
      .setInPlaceChoice(NoInPlaceChoice().setOutputColumn(outputColumnName))
      .setInputColumn(NameSingleColumnSelection(inputColumnName))
    set(singleOrMultiChoiceParameter, choice)
  }

  def setSelectedColumns(value: MultipleColumnSelection): this.type = {
    val multiChoice = MultiColumnChoice().setInputColumns(value).setInPlaceChoice(MultiColumnYesInPlace())
    setSingleOrMultiChoice(multiChoice)
  }
}