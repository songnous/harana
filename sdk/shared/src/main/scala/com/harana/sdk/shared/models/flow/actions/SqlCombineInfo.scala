package com.harana.sdk.shared.models.flow.actions

import com.harana.sdk.shared.models.common.Version
import com.harana.sdk.shared.models.flow.catalogs.ActionCategory.SetAction
import com.harana.sdk.shared.models.flow.{Action2To1Info, PortPosition}
import com.harana.sdk.shared.models.flow.actions.dataframe.DataFrameInfo
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
  val description = "Combines two DataFrames into one using custom SQL"
  val since = Version(1, 4, 0)
  val category = SetAction

  override val inPortsLayout = Vector(PortPosition.Left, PortPosition.Right)

  val leftTableNameParameter = StringParameter("Left dataframe id", description = Some("The identifier that can be used in the Spark SQL expression to refer the left-hand side DataFrame."))
  setDefault(leftTableNameParameter, "")
  def getLeftTableName = $(leftTableNameParameter)
  def setLeftTableName(name: String): this.type = set(leftTableNameParameter, name)

  val rightTableNameParameter = StringParameter("Right dataframe id", Some("The identifier that can be used in the Spark SQL expression to refer the right-hand side DataFrame."))
  setDefault(rightTableNameParameter, "")
  def getRightTableName = $(rightTableNameParameter)
  def setRightTableName(name: String): this.type = set(rightTableNameParameter, name)

  val sqlCombineExpressionParameter = CodeSnippetParameter("expression", Some("SQL expression to be executed on two DataFrames, yielding a DataFrame."),
    language = CodeSnippetLanguage(CodeSnippetLanguage.sql)
  )
  setDefault(sqlCombineExpressionParameter, "")
  def getSqlCombineExpression = $(sqlCombineExpressionParameter)
  def setSqlCombineExpression(expression: String): this.type = set(sqlCombineExpressionParameter, expression)

  override def customValidateParameters =
    if (getLeftTableName == getRightTableName)
      Vector(ParametersEqualError("left dataframe id", "right dataframe id", getLeftTableName))
    else
      Vector.empty[FlowError]

  val parameters = Array(leftTableNameParameter, rightTableNameParameter, sqlCombineExpressionParameter)

  @transient
  lazy val portI_0: ru.TypeTag[DataFrameInfo] = ru.typeTag[DataFrameInfo]

  @transient
  lazy val portI_1: ru.TypeTag[DataFrameInfo] = ru.typeTag[DataFrameInfo]

  @transient
  lazy val portO_0: ru.TypeTag[DataFrameInfo] = ru.typeTag[DataFrameInfo]

}

object SqlCombineInfo extends SqlCombineInfo