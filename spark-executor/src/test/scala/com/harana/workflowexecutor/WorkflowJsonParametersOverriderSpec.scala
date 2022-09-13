package com.harana.workflowexecutor

import com.harana.sdk.backend.models.flow.json.StandardSpec
import org.scalatest.BeforeAndAfter
import org.scalatestplus.mockito.MockitoSugar


class WorkflowJsonParametersOverriderSpec extends StandardSpec with BeforeAndAfter with MockitoSugar with DefaultJsonProtocol {

  "WorkflowJsonParametersOverrider" should {

    "override parameters based on passed extra parameters" in {
      val overrides = Map(
        "node1.parameter with spaces" -> "new value",
        "node2.nested.parameter.test" -> "changed"
      )

      WorkflowJsonParametersOverrider.overrideParameters(originalJson, overrides) shouldBe expectedJson
    }

    "throw when invalid parameters are passed" in {
      val overrides = Map(
        "node1.no such parameter" -> "no such parameter",
        "no such node.parameter" -> "no such node"
      )

      a[RuntimeException] should be thrownBy {
        WorkflowJsonParametersOverrider.overrideParameters(originalJson, overrides)
      }
    }
  }

  val originalJson =
    """{
      |  "workflow": {
      |    "nodes": [{
      |      "id": "node1",
      |      "parameters": {
      |        "parameter with spaces": "value"
      |      }
      |     }, {
      |      "id": "node2",
      |      "parameters": {
      |        "parameter": "value",
      |        "nested": {
      |          "parameter": {
      |            "test": "nested value"
      |          }
      |        }
      |      }
      |    }]
      |  }
      |}
    """.stripMargin.parseJson

  val expectedJson =
    """{
      |  "workflow": {
      |    "nodes": [{
      |      "id": "node1",
      |      "parameters": {
      |        "parameter with spaces": "new value"
      |      }
      |     }, {
      |      "id": "node2",
      |      "parameters": {
      |        "parameter": "value",
      |        "nested": {
      |          "parameter": {
      |            "test": "changed"
      |          }
      |        }
      |      }
      |    }]
      |  }
      |}
    """.stripMargin.parseJson

}
