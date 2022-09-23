package com.harana.sdk.shared.models.flow.actions

import com.harana.sdk.shared.models.common.Version
import com.harana.sdk.shared.models.flow.actionobjects.DataFrameInfo
import com.harana.sdk.shared.models.flow.catalogs.ActionCategory.SetAction
import com.harana.sdk.shared.models.flow.{Action2To1Info, PortPosition}
import com.harana.sdk.shared.models.flow.catalogs.ActionCategory.SetAction
import com.harana.sdk.shared.models.flow.documentation.ActionDocumentation
import com.harana.sdk.shared.models.flow.exceptions.FlowError
import com.harana.sdk.shared.models.flow.parameters.{CodeSnippetLanguage, CodeSnippetParameter, StringParameter}
import com.harana.sdk.shared.models.flow.parameters.exceptions.ParametersEqualError
import com.harana.sdk.shared.models.flow.utils.Id

import scala.reflect.runtime.{universe => ru}

trait SqlCombineInfo extends Action2To1Info[DataFrameInfo, DataFrameInfo, DataFrameInfo]
  with ActionDocumentation {

  val id: Id = "8f254d75-276f-48b7-872d-e4a18b6a86c6"
  val name = "SQL Combine"
  val since = Version(1, 4, 0)
  val category = SetAction

  override val inputPortsLayout = List(PortPosition.Left, PortPosition.Right)

  val leftTableNameParameter = StringParameter("Left dataframe id")
  setDefault(leftTableNameParameter, "")
  def getLeftTableName = $(leftTableNameParameter)
  def setLeftTableName(name: String): this.type = set(leftTableNameParameter, name)

  val rightTableNameParameter = StringParameter("Right dataframe id")
  setDefault(rightTableNameParameter, "")
  def getRightTableName = $(rightTableNameParameter)
  def setRightTableName(name: String): this.type = set(rightTableNameParameter, name)

  val sqlCombineExpressionParameter = CodeSnippetParameter("expression",
    language = CodeSnippetLanguage(CodeSnippetLanguage.sql)
  )
  setDefault(sqlCombineExpressionParameter, "")
  def getSqlCombineExpression = $(sqlCombineExpressionParameter)
  def setSqlCombineExpression(expression: String): this.type = set(sqlCombineExpressionParameter, expression)

  override def customValidateParameters =
    if (getLeftTableName == getRightTableName)
      List(ParametersEqualError("left dataframe id", "right dataframe id", getLeftTableName))
    else
      List.empty[FlowError]

  val parameters = Left(Array(leftTableNameParameter, rightTableNameParameter, sqlCombineExpressionParameter))

  @transient
  lazy val portI_0: ru.TypeTag[DataFrameInfo] = ru.typeTag[DataFrameInfo]

  @transient
  lazy val portI_1: ru.TypeTag[DataFrameInfo] = ru.typeTag[DataFrameInfo]

  @transient
  lazy val portO_0: ru.TypeTag[DataFrameInfo] = ru.typeTag[DataFrameInfo]

}

object SqlCombineInfo extends SqlCombineInfo {
  def apply(pos: (Int, Int), color: Option[String] = None) = new SqlCombineInfo {
    override val position = Some(pos)
    override val overrideColor = color
  }
}