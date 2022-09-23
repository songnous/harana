package com.harana.sdk.shared.models.flow.actionobjects

import com.harana.sdk.shared.models.flow.actionobjects.multicolumn.MultiColumnParameters.MultiColumnInPlaceChoices.MultiColumnYesInPlace
import com.harana.sdk.shared.models.flow.actionobjects.multicolumn.MultiColumnParameters.SingleOrMultiColumnChoice
import com.harana.sdk.shared.models.flow.actionobjects.multicolumn.MultiColumnParameters.SingleOrMultiColumnChoices.{MultiColumnChoice, SingleColumnChoice}
import com.harana.sdk.shared.models.flow.actionobjects.multicolumn.SingleColumnParameters.SingleTransformInPlaceChoices.NoInPlaceChoice
import com.harana.sdk.shared.models.flow.actionobjects.multicolumn.HasSpecificParameters
import com.harana.sdk.shared.models.flow.actionobjects.multicolumn.MultiColumnParameters.SingleOrMultiColumnChoice
import com.harana.sdk.shared.models.flow.parameters.selections.{MultipleColumnSelection, NameSingleColumnSelection}
import com.harana.sdk.shared.models.flow.parameters.{IOColumnsParameter, Parameter}

trait MultiColumnTransformerInfo extends TransformerInfo with HasSpecificParameters {

  val singleOrMultiChoiceParameter = IOColumnsParameter()
  setDefault(singleOrMultiChoiceParameter, SingleColumnChoice())
  def getSingleOrMultiChoice = $(singleOrMultiChoiceParameter)
  def setSingleOrMultiChoice(value: SingleOrMultiColumnChoice): this.type = set(singleOrMultiChoiceParameter, value)

  override lazy val parameters =
    Left(
      if (specificParameters == null) Array(singleOrMultiChoiceParameter)
      else specificParameters :+ singleOrMultiChoiceParameter
    )

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