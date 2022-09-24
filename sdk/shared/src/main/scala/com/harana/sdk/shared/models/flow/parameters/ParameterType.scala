package com.harana.sdk.shared.models.flow.parameters

import enumeratum.{Enum, EnumEntry}

sealed trait ParameterType extends EnumEntry

case object ParameterType extends Enum[ParameterType] {
  case object Boolean extends ParameterType
  case object Numeric extends ParameterType
  case object MultipleNumeric extends ParameterType
  case object MultipleString extends ParameterType
  case object Password extends ParameterType
  case object String extends ParameterType
  case object Email extends ParameterType
  case object HTML extends ParameterType
  case object Choice extends ParameterType
  case object SearchQuery extends ParameterType
  case object Emoji extends ParameterType
  case object Country extends ParameterType
  case object TimeZone extends ParameterType
  case object Tags extends ParameterType
  case object Color extends ParameterType
  case object LongArray extends ParameterType
  case object IPAddress extends ParameterType
  case object DataSource extends ParameterType
  case object URI extends ParameterType
  case object MultipleChoice extends ParameterType
  case object TimestampParts extends ParameterType
  case object Multiplier extends ParameterType
  case object ColumnSelector extends ParameterType
  case object JSON extends ParameterType
  case object Markdown extends ParameterType
  case object SingleColumnCreator extends ParameterType
  case object MultipleColumnCreator extends ParameterType
  case object TimestampColumns extends ParameterType
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