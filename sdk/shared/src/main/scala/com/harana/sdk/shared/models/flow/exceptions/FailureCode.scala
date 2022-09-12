package com.harana.sdk.shared.models.flow.exceptions

import enumeratum.values._

sealed abstract class FailureCode(val value: Int) extends IntEnumEntry

case object FailureCode extends IntEnum[FailureCode] with IntCirceEnum[FailureCode] {
  case object NodeFailure extends FailureCode(1)
  case object LaunchingFailure extends FailureCode(2)
  case object WorkflowNotFound extends FailureCode(3)
  case object CannotUpdateRunningWorkflow extends FailureCode(4)
  case object EntityNotFound extends FailureCode(5)
  case object UnexpectedError extends FailureCode(6)
  case object IllegalArgumentException extends FailureCode(7)
  case object IncorrectWorkflow extends FailureCode(8)
  case object IncorrectNode extends FailureCode(9)
  val values = findValues
}