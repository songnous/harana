package com.harana.sdk.shared.models.flow.actionobjects.multicolumn

import com.harana.sdk.shared.models.flow.actionobjects.multicolumn.MultiColumnParameters.MultiColumnInPlaceChoices.MultiColumnYesInPlace
import com.harana.sdk.shared.models.flow.actionobjects.multicolumn.SingleColumnParameters.SingleColumnInPlaceChoice
import com.harana.sdk.shared.models.flow.parameters.choice.Choice.ChoiceOption
import com.harana.sdk.shared.models.flow.parameters.choice.{Choice, ChoiceParameter}
import com.harana.sdk.shared.models.flow.parameters.selections.{MultipleColumnSelection, NameColumnSelection, SingleColumnSelection}
import com.harana.sdk.shared.models.flow.parameters.{ColumnSelectorParameter, ParameterGroup, PrefixBasedColumnCreatorParameter, SingleColumnSelectorParameter}

object MultiColumnParameters {

  sealed abstract class MultiColumnInPlaceChoice extends Choice {
    val choiceOrder = MultiColumnInPlaceChoices.choiceOrder
  }

  object MultiColumnInPlaceChoices {
    val choiceOrder: List[ChoiceOption] = List(classOf[MultiColumnYesInPlace], classOf[MultiColumnNoInPlace])

    case class MultiColumnYesInPlace() extends MultiColumnInPlaceChoice {
      val name = "replace-input-columns"
      override val parameterGroups = List.empty[ParameterGroup]
    }

    case class MultiColumnNoInPlace() extends MultiColumnInPlaceChoice {
      val name = "append-new-columns"

      val outputColumnsPrefixParameter = PrefixBasedColumnCreatorParameter("column-name-prefix")
      def getColumnsPrefix = $(outputColumnsPrefixParameter)
      def setColumnsPrefix(prefix: String): this.type = set(outputColumnsPrefixParameter, prefix)

      override val parameterGroups = List(ParameterGroup("", outputColumnsPrefixParameter))
    }
  }

  sealed abstract class SingleOrMultiColumnChoice extends Choice {
    val choiceOrder = SingleOrMultiColumnChoices.choiceOrder
  }

  object SingleOrMultiColumnChoices {
    val choiceOrder: List[ChoiceOption] = List(classOf[SingleColumnChoice], classOf[MultiColumnChoice])

    case class SingleColumnChoice() extends SingleOrMultiColumnChoice with HasSingleInPlaceParameter {
      val name = "one-column"

      val inputColumnParameter = SingleColumnSelectorParameter("input-column", portIndex = 0)
      def getInputColumn = $(inputColumnParameter)
      def setInputColumn(value: SingleColumnSelection): this.type = set(inputColumnParameter, value)

      def getInPlaceChoice = $(singleInPlaceChoiceParameter)
      def setInPlaceChoice(value: SingleColumnInPlaceChoice): this.type = set(singleInPlaceChoiceParameter, value)

      override val parameterGroups = List(ParameterGroup("", inputColumnParameter, singleInPlaceChoiceParameter))
    }

    case class MultiColumnChoice() extends SingleOrMultiColumnChoice {
      val name = "multiple-columns"

      val inputColumnsParameter = ColumnSelectorParameter("input-columns", portIndex = 0)
      def getInputColumns = $(inputColumnsParameter)
      def setInputColumns(value: MultipleColumnSelection): this.type = set(inputColumnsParameter, value)
      def setInputColumns(inputColumnNames: Set[String]): this.type = setInputColumns(MultipleColumnSelection(List(NameColumnSelection(inputColumnNames))))

      val inPlaceChoiceParameter = ChoiceParameter[MultiColumnInPlaceChoice]("output", default = Some(MultiColumnYesInPlace()))
      def getInPlaceChoice = $(inPlaceChoiceParameter)
      def setInPlaceChoice(value: MultiColumnInPlaceChoice): this.type = set(inPlaceChoiceParameter, value)

      override val parameterGroups = List(ParameterGroup("", inputColumnsParameter, inPlaceChoiceParameter))
    }

    object MultiColumnChoice {
      def apply(inputColumnNames: Set[String]): MultiColumnChoice = MultiColumnChoice().setInputColumns(inputColumnNames)
    }
  }
}
