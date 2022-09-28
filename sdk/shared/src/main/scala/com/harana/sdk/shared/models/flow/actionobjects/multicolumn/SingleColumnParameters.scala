package com.harana.sdk.shared.models.flow.actionobjects.multicolumn

import SingleColumnParameters.SingleTransformInPlaceChoices.{NoInPlaceChoice, YesInPlaceChoice}
import com.harana.sdk.shared.models.flow.parameters.{Parameter, ParameterGroup, SingleColumnCreatorParameter}
import com.harana.sdk.shared.models.flow.parameters.choice.Choice

object SingleColumnParameters {

  sealed abstract class SingleColumnInPlaceChoice extends Choice {
    val choiceOrder: List[Class[_ <: Choice]] = List(classOf[YesInPlaceChoice], classOf[NoInPlaceChoice])
  }

  object SingleTransformInPlaceChoices {

    case class YesInPlaceChoice() extends SingleColumnInPlaceChoice {
      val name = "replace input column"
      val parameterGroups = List.empty[ParameterGroup]
    }

    case class NoInPlaceChoice() extends SingleColumnInPlaceChoice {
      val name = "append new column"

      val outputColumnParameter = SingleColumnCreatorParameter("output column")
      def getOutputColumn = $(outputColumnParameter)
      def setOutputColumn(columnName: String): this.type = set(outputColumnParameter, columnName)

      val parameterGroups = List(ParameterGroup(None, outputColumnParameter))
    }
  }
}