package com.harana.sdk.shared.models.flow.graph.node

sealed trait NodeStatusName

object NodeStatusName {
  case object Queued extends NodeStatusName
  case object Draft extends NodeStatusName
  case object Running extends NodeStatusName
  case object Completed extends NodeStatusName
  case object Aborted extends NodeStatusName
  case object Failed extends NodeStatusName
}