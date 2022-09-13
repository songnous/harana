package com.harana.designer.backend.services

import com.harana.sdk.shared.models.common.{Background, ParameterValue, Visibility}
import com.harana.sdk.shared.models.common.User.UserId
import com.harana.sdk.backend.models.flow.execution.ExecutionStatus
import com.harana.sdk.backend.models.flow._

package object user {

  def createSampleFlow(userId: UserId): (Flow, FlowExecution) = {
    val inPort = Port.Int("In")
    val outPort = Port.Int("Out")

    val parameters = Map(
      "string" -> ParameterValue.String("a"),
      "stringList" -> ParameterValue.StringList("one" :: "two" :: "three" :: Nil),
      "integer" -> ParameterValue.Integer(4),
      "integerList" -> ParameterValue.IntegerList(2 :: 3 :: 4 :: Nil),
      "integerRange" -> ParameterValue.IntegerRange((2, 3))
    )

//    val action1 = Action("Get Oracle", "Gets Oracle", 50, 50, "#EC407A", new GetOracleInfo, parameters)
//    val action2 = Action("Get Exasol", "Get Exasol", 250, 50, "#7E547C2", new GetExasolInfo, parameters)
//    val action3 = Action("Put SQLServer", "Put SQLServer", 450, 50, "#7E57C2", new PutSqlServerInfo, parameters)
//    val actions = List(action1, action2, action3)
//
//    val link1 = Link(action1.id, outPort, action2.id, inPort)
//    val link2 = Link(action2.id, outPort, action3.id, inPort)
//    val links = List(link1, link2)
//
    val flow = Flow("Test Flow", "This is a description about this flow", List(), List(), List(), Some(userId), Visibility.Owner, None, Set("Flow"))
    val execution = FlowExecution(flow.id, Some(userId), ExecutionStatus.PendingExecution, Some(0), Some(0))
    (flow, execution)
  }
}