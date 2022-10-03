package com.harana.sdk.backend.models.flow.json.graph

import com.harana.sdk.backend.models.flow.actiontypes.ActionType
import com.harana.sdk.shared.models.designer.flow.graph.Endpoint
import com.harana.sdk.shared.models.flow.ActionTypeInfo
import io.circe.Json
import org.mockito.Mockito._
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import org.scalatestplus.mockito.MockitoSugar

trait GraphJsonTestSupport extends AnyWordSpec with MockitoSugar with Matchers {

  def assertEndpointMatchesJson(edgeEnd: Endpoint, edgeEndJs: Json) = {
    assert(edgeEndJs.fields("nodeId").as[String] == edgeEnd.nodeId.value.toString)
    assert(edgeEndJs.fields("portIndex").as[Int] == edgeEnd.portIndex)
  }

  def endpointMatchesJson(edgeEnd: Endpoint, edgeEndJs: Json): Boolean =
    edgeEndJs.fields("nodeId").as[String] == edgeEnd.nodeId.value.toString &&
      edgeEndJs.fields("portIndex").as[Int] == edgeEnd.portIndex

  def mockAction(inArity: Int, outArity: Int, id: ActionTypeInfo.Id, name: String): ActionType = {

    val action = mock[ActionType]
    when(action.inArity).thenReturn(inArity)
    when(action.outArity).thenReturn(outArity)
    when(action.id).thenReturn(id)
    when(action.name).thenReturn(name)
    action
  }
}
