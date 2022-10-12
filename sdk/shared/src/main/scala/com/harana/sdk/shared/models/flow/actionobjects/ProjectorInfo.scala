package com.harana.sdk.shared.models.flow.actionobjects

import com.harana.sdk.shared.models.flow.parameters._
import com.harana.sdk.shared.models.flow.parameters.choice.Choice.ChoiceOption
import com.harana.sdk.shared.models.flow.parameters.choice.{Choice, ChoiceParameter}
import com.harana.sdk.shared.models.flow.parameters.selections.SingleColumnSelection

trait ProjectorInfo extends TransformerInfo {
  import ProjectorInfo._

  val id = "5867276F-3897-4E11-B141-F58CE0F5EBFE"

  val projectionColumnsParameter = ParametersSequence[ColumnProjection]("projection columns")
  def getProjectionColumns = $(projectionColumnsParameter)
  def setProjectionColumns(value: Seq[ColumnProjection]): this.type = set(projectionColumnsParameter, value)

  override val parameterGroups = List(ParameterGroup("", projectionColumnsParameter))
}

object ProjectorInfo extends ProjectorInfo{

  val originalColumnParameterName = "original column"
  val renameColumnParameterName = "rename column"

  case class ColumnProjection() extends Parameters {

    val originalColumnParameter = SingleColumnSelectorParameter("original-column", portIndex = 0)
    def getOriginalColumn = $(originalColumnParameter)
    def setOriginalColumn(value: SingleColumnSelection): this.type = set(originalColumnParameter, value)

    val renameColumnParameter = ChoiceParameter[RenameColumnChoice]("rename-column", default = Some(RenameColumnChoice.No()))
    def getRenameColumn = $(renameColumnParameter)
    def setRenameColumn(value: RenameColumnChoice): this.type = set(renameColumnParameter, value)

    override val parameterGroups = List(ParameterGroup("", originalColumnParameter, renameColumnParameter))

  }

  sealed trait RenameColumnChoice extends Choice {
    import RenameColumnChoice._

    def getColumnName: Option[String]
    val choiceOrder: List[ChoiceOption] = List(classOf[No], classOf[Yes])
  }

  object RenameColumnChoice {
    case class Yes() extends RenameColumnChoice {
      val name = "yes"

      val columnNameParameter = SingleColumnCreatorParameter("column-name", default = Some(""))
      def getColumnName: Option[String] = Some($(columnNameParameter))
      def setColumnName(value: String): this.type = set(columnNameParameter, value)

      override val parameterGroups = List(ParameterGroup("", columnNameParameter))
    }

    case class No() extends RenameColumnChoice {
      val name = "no"
      def getColumnName: Option[String] = None
      override val parameterGroups = List.empty[ParameterGroup]
    }
  }
}