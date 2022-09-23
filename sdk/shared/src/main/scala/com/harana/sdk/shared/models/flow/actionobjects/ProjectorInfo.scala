package com.harana.sdk.shared.models.flow.actionobjects

import com.harana.sdk.shared.models.flow.parameters.choice.Choice.ChoiceOption
import com.harana.sdk.shared.models.flow.parameters.choice.{Choice, ChoiceParameter}
import com.harana.sdk.shared.models.flow.parameters.selections.SingleColumnSelection
import com.harana.sdk.shared.models.flow.parameters._

trait ProjectorInfo extends TransformerInfo {
  import ProjectorInfo._

  val id = "5867276F-3897-4E11-B141-F58CE0F5EBFE"

  val projectionColumnsParameter = ParametersSequence[ColumnProjection]("projection columns")
  def getProjectionColumns = $(projectionColumnsParameter)
  def setProjectionColumns(value: Seq[ColumnProjection]): this.type = set(projectionColumnsParameter, value)

  val parameters = Left(Array(projectionColumnsParameter))
}

object ProjectorInfo extends ProjectorInfo{

  val originalColumnParameterName = "original column"
  val renameColumnParameterName = "rename column"

  case class ColumnProjection() extends Parameters {

    val originalColumnParameter = SingleColumnSelectorParameter("original column", portIndex = 0)
    def getOriginalColumn = $(originalColumnParameter)
    def setOriginalColumn(value: SingleColumnSelection): this.type = set(originalColumnParameter, value)

    val renameColumnParameter = ChoiceParameter[RenameColumnChoice]("rename column")
    setDefault(renameColumnParameter, RenameColumnChoice.No())
    def getRenameColumn = $(renameColumnParameter)
    def setRenameColumn(value: RenameColumnChoice): this.type = set(renameColumnParameter, value)

    val parameters = Left(Array(originalColumnParameter, renameColumnParameter))

  }

  sealed trait RenameColumnChoice extends Choice {
    import RenameColumnChoice._

    def getColumnName: Option[String]
    val choiceOrder: List[ChoiceOption] = List(classOf[No], classOf[Yes])
  }

  object RenameColumnChoice {
    case class Yes() extends RenameColumnChoice {
      val name = "Yes"

      val columnNameParameter = SingleColumnCreatorParameter("column name")
      setDefault(columnNameParameter, "")
      def getColumnName: Option[String] = Some($(columnNameParameter))
      def setColumnName(value: String): this.type = set(columnNameParameter, value)

      val parameters = Left(Array(columnNameParameter))
    }

    case class No() extends RenameColumnChoice {
      val name = "No"
      def getColumnName: Option[String] = None
      val parameters = Left(Array.empty[Parameter[_]])
    }
  }
}