package com.harana.sdk.shared.models.flow.exceptions

case class CyclicGraphError() extends HaranaError {
  val code = FailureCode.IllegalArgumentException
  val title = "Cyclic graph"
  val message = "Graph cycle detected"
}