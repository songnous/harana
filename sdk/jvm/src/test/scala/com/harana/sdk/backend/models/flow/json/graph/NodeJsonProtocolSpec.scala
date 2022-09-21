package com.harana.sdk.backend.models.flow.json.graph

import com.harana.models.json.graph.GraphJsonTestSupport
import com.harana.sdk.backend.models.flow.ActionType
import com.harana.sdk.shared.models.flow.ActionTypeInfo
import com.harana.sdk.shared.models.flow.graph.FlowGraph.{FlowNode, flowNodeEncoder}
import com.harana.sdk.shared.models.flow.graph.node.Node
import io.circe.syntax.EncoderOps
import org.mockito.Mockito._

class NodeJsonProtocolSpec extends GraphJsonTestSupport {

  "Node with action transformed to Json" should {
    val expectedActionId = ActionTypeInfo.Id.randomId
    val expectedActionName = "expectedName"
    val action = mock[ActionType]

    when(action.id).thenReturn(expectedActionId)
    when(action.name).thenReturn(expectedActionName)

    val node = mock[FlowNode]
    val expectedNodeId = Node.Id.randomId
    when(node.value).thenReturn(action)
    when(node.id).thenReturn(expectedNodeId)
    val nodeJson = node.asJson

    "have correct 'id' field" in {
      nodeJson.hcursor.downField("id").as[String] shouldBe expectedNodeId.toString
    }

    "have correct 'action' field" in {
      val actionField = nodeJson.hcursor.downField("action")
      actionField.downField("id").as[ActionTypeInfo.Id] shouldBe expectedActionId
      actionField.downField("name").as[String] shouldBe expectedActionName
    }
  }
}