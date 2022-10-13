package com.harana.sdk.shared.models.flow.actiontypes

import com.harana.sdk.shared.models.common.Version
import com.harana.sdk.shared.models.flow.actionobjects.DataFrameInfo
import com.harana.sdk.shared.models.flow.catalogs.ActionCategory.SetAction
import com.harana.sdk.shared.models.flow.documentation.ActionDocumentation
import com.harana.sdk.shared.models.flow.exceptions.FlowError
import com.harana.sdk.shared.models.flow.parameters.exceptions.ParametersEqualError
import com.harana.sdk.shared.models.flow.parameters.{CodeSnippetLanguage, CodeSnippetParameter, ParameterGroup, StringParameter}
import com.harana.sdk.shared.models.flow.utils.Id
import com.harana.sdk.shared.models.flow.{Action2To1TypeInfo, PortPosition}
import izumi.reflect.Tag

import scala.reflect.runtime.{universe => ru}

trait SqlCombineInfo extends Action2To1TypeInfo[DataFrameInfo, DataFrameInfo, DataFrameInfo]
  with ActionDocumentation {

  val id: Id = "8f254d75-276f-48b7-872d-e4a18b6a86c6"
  val name = "sql-combine"
  val since = Version(1, 4, 0)
  val category = SetAction

  override val inputPortsLayout = List(PortPosition.Left, PortPosition.Right)

  val leftTableNameParameter = StringParameter("left-table-name", default = Some(""))
  def getLeftTableName = $(leftTableNameParameter)
  def setLeftTableName(name: String): this.type = set(leftTableNameParameter, name)

  val rightTableNameParameter = StringParameter("right-table-name", default = Some(""))
  def getRightTableName = $(rightTableNameParameter)
  def setRightTableName(name: String): this.type = set(rightTableNameParameter, name)

  val sqlCombineExpressionParameter = CodeSnippetParameter("expression", default = Some(""), language = CodeSnippetLanguage.SQL)
  def getSqlCombineExpression = $(sqlCombineExpressionParameter)
  def setSqlCombineExpression(expression: String): this.type = set(sqlCombineExpressionParameter, expression)

  override def customValidateParameters =
    if (getLeftTableName == getRightTableName)
      List(ParametersEqualError("left-table-name", "right-table-name", getLeftTableName))
    else
      List.empty[FlowError]

  override val parameterGroups = List(ParameterGroup("", leftTableNameParameter, rightTableNameParameter, sqlCombineExpressionParameter))

  @transient
  lazy val portI_0: Tag[DataFrameInfo] = Tag[DataFrameInfo]

  @transient
  lazy val portI_1: Tag[DataFrameInfo] = Tag[DataFrameInfo]

  @transient
  lazy val portO_0: Tag[DataFrameInfo] = Tag[DataFrameInfo]

}

object SqlCombineInfo extends SqlCombineInfo