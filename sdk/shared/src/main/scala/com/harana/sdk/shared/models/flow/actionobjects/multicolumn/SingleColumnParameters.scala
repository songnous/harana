package com.harana.sdk.shared.models.flow.actionobjects.multicolumn

import com.harana.sdk.shared.models.flow.actionobjects.multicolumn.SingleColumnParameters.SingleTransformInPlaceChoices.{NoInPlaceChoice, YesInPlaceChoice}
import com.harana.sdk.shared.models.flow.parameters.choice.Choice
import com.harana.sdk.shared.models.flow.parameters.{ParameterGroup, SingleColumnCreatorParameter}

object SingleColumnParameters {

  sealed abstract class SingleColumnInPlaceChoice extends Choice {
    val choiceOrder: List[Class[_ <: Choice]] = List(classOf[YesInPlaceChoice], classOf[NoInPlaceChoice])
  }

  object SingleTransformInPlaceChoices {

    case class YesInPlaceChoice() extends SingleColumnInPlaceChoice {
      val name = "replace input column"
      override val parameterGroups = List.empty[ParameterGroup]
    }

    case class NoInPlaceChoice() extends SingleColumnInPlaceChoice {
      val name = "append new column"

      val outputColumnParameter = SingleColumnCreatorParameter("output column")
      def getOutputColumn = $(outputColumnParameter)
      def setOutputColumn(columnName: String): this.type = set(outputColumnParameter, columnName)

      override val parameterGroups = List(ParameterGroup("", outputColumnParameter))
    }
  }
}