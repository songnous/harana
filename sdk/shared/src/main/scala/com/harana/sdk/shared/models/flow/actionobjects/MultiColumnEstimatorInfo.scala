package com.harana.sdk.shared.models.flow.actionobjects

import com.harana.sdk.shared.models.flow.actionobjects.multicolumn.MultiColumnParameters.MultiColumnInPlaceChoices.{MultiColumnNoInPlace, MultiColumnYesInPlace}
import com.harana.sdk.shared.models.flow.actionobjects.multicolumn.MultiColumnParameters.SingleOrMultiColumnChoices.{MultiColumnChoice, SingleColumnChoice}
import com.harana.sdk.shared.models.flow.actionobjects.multicolumn.SingleColumnParameters.SingleTransformInPlaceChoices.{NoInPlaceChoice, YesInPlaceChoice}
import com.harana.sdk.shared.models.flow.actionobjects.multicolumn.HasSpecificParameters
import com.harana.sdk.shared.models.flow.parameters.{IOColumnsParameter, Parameter}
import com.harana.sdk.shared.models.flow.parameters.selections.NameSingleColumnSelection

import scala.language.reflectiveCalls

trait MultiColumnEstimatorInfo extends EstimatorInfo with HasSpecificParameters {

  val id = "D10B7F69-159D-43BA-8746-7B907138F061"

  val singleOrMultiChoiceParameter = IOColumnsParameter()

  lazy val parameters =
    Left(
      if (specificParameters == null) List(singleOrMultiChoiceParameter)
      else specificParameters.toList :+ singleOrMultiChoiceParameter
    )

  def setSingleColumn(inputColumnName: String, outputColumnName: String) = {
    val choice = SingleColumnChoice().setInPlaceChoice(NoInPlaceChoice().setOutputColumn(outputColumnName)).setInputColumn(NameSingleColumnSelection(inputColumnName))
    set(singleOrMultiChoiceParameter, choice)
  }

  def setSingleColumnInPlace(inputColumnName: String) = {
    val choice = SingleColumnChoice().setInPlaceChoice(YesInPlaceChoice()).setInputColumn(NameSingleColumnSelection(inputColumnName))
    set(singleOrMultiChoiceParameter, choice)
  }

  def setMultipleColumn(inputColumnNames: Set[String], outputColumnPrefix: String) = {
    val choice = MultiColumnChoice(inputColumnNames).setInPlaceChoice(MultiColumnNoInPlace().setColumnsPrefix(outputColumnPrefix))
    set(singleOrMultiChoiceParameter, choice)
  }

  def setMultipleColumnInPlace(inputColumnNames: Set[String]) = {
    val choice = MultiColumnChoice(inputColumnNames).setInPlaceChoice(MultiColumnYesInPlace())
    set(singleOrMultiChoiceParameter, choice)
  }
}

object MultiColumnEstimatorInfo extends MultiColumnEstimatorInfo {
  val specificParameters = Array.empty[Parameter[_]]
}
