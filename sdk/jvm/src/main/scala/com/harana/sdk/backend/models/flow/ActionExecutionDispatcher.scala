package com.harana.sdk.backend.models.flow

import com.harana.sdk.shared.models.flow.utils.Id
import scala.collection.concurrent.TrieMap
import scala.concurrent.{Future, Promise}

class ActionExecutionDispatcher {

  import ActionExecutionDispatcher._

  private val actionEndPromises: TrieMap[ActionId, Promise[Result]] = TrieMap.empty

  def executionStarted(workflowId: Id, nodeId: Id) = {
    val promise: Promise[Result] = Promise()
    require(actionEndPromises.put((workflowId, nodeId), promise).isEmpty)
    promise.future
  }

  def executionEnded(workflowId: Id, nodeId: Id, result: Result) = {
    val promise = actionEndPromises.remove((workflowId, nodeId))
    require(promise.nonEmpty)
    promise.get.success(result)
  }
}

object ActionExecutionDispatcher {
  type ActionId = (Id, Id)
  type Error = String
  type Result = Either[Error, Unit]
}
