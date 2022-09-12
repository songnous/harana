package com.harana.sdk.shared.models.flow.parameters

import enumeratum.{Enum, EnumEntry}

sealed trait ParameterType extends EnumEntry

case object ParameterType extends Enum[ParameterType] {
  case object Boolean extends ParameterType
  case object Numeric extends ParameterType
  case object MultipleNumeric extends ParameterType
  case object String extends ParameterType
  case object Choice extends ParameterType
  case object MultipleChoice extends ParameterType
  case object Multiplier extends ParameterType
  case object ColumnSelector extends ParameterType
  case object SingleColumnCreator extends ParameterType
  case object MultipleColumnCreator extends ParameterType
  case object PrefixBasedColumnCreator extends ParameterType
  case object CodeSnippet extends ParameterType
  case object Dynamic extends ParameterType
  case object Workflow extends ParameterType
  case object GridSearch extends ParameterType
  case object LoadFromLibrary extends ParameterType
  case object SaveToLibrary extends ParameterType
  case object DatasourceIdForRead extends ParameterType
  case object DatasourceIdForWrite extends ParameterType
  val values = findValues
}