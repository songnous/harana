package com.harana.sdk.backend.models.flow.json.graph

import com.harana.models.json.graph.GraphJsonTestSupport
import com.harana.sdk.shared.models.designer.flow.graph.node._
import com.harana.sdk.backend.models.designer.flow.json.DateTimeJsonProtocol.DateTimeJsonFormat
import com.harana.sdk.backend.models.designer.flow.json.IdJsonProtocol.IdFormat
import com.harana.sdk.backend.models.designer.flow.json.graph.NodeStatusJsonProtocol.NodeStatusFormat
import com.harana.sdk.shared.models.designer.flow.exceptions
import com.harana.sdk.shared.models.designer.flow.exceptions.json.FailureDescriptionJsonProtocol
import com.harana.sdk.shared.models.designer.flow.utils.{DateTimeConverter}
import com.harana.sdk.shared.models.flow.exceptions.{FailureCode, FailureDescription, HaranaFile}
import com.harana.sdk.shared.models.flow.graph.node.NodeStatus
import com.harana.sdk.shared.models.flow.utils.Entity
import io.circe.syntax.EncoderOps

import java.time.Instant
import java.time.temporal.ChronoUnit


class NodeStatusJsonProtocolSpec extends GraphJsonTestSupport {

  val error = FailureDescription(
    HaranaFile.Id.randomId,
    FailureCode.CannotUpdateRunningWorkflow,
    "This is a test FailureDescription",
    Some("This is a long test description"),
    Map("detail1" -> "value1", "detail2" -> "value2")
  )

  val results = Seq(Entity.Id.randomId, Entity.Id.randomId, Entity.Id.randomId)

  val started = Instant.now
  val ended = started.plus(1, ChronoUnit.DAYS)
  val failed = NodeStatus.Failed(started, ended, error)
  val completed = NodeStatus.Completed(started, ended, results)
  val running = NodeStatus.Running(started, results)

  val failedJson = js("FAILED", "started" -> started.asJson, "ended" -> ended.asJson, "error" -> error.asJson)
  val completedJson = js("COMPLETED", "started" -> started.asJson, "ended" -> ended.asJson, "results" -> results.asJson)
  val runningJson = js("RUNNING", "started" -> started.asJson, "results" -> results.asJson)
  val abortedJson = js("ABORTED", "results" -> results.asJson)
  val queuedJson = js("QUEUED", "results" -> results.asJson)
  val draftJson = js("DRAFT", "results" -> results.asJson)

  "NodeStateJsonProtocol" should {

    "transform Draft to Json" in {
      toJs(NodeStatus.Draft(results)) shouldBe draftJson
    }

    "read Draft from Json" in {
      fromJs(draftJson) shouldBe NodeStatus.Draft(results)
    }

    "transform Queued to Json" in {
      toJs(NodeStatus.Queued(results)).asJson shouldBe queuedJson
    }

    "read Queued from Json" in {
      fromJs(queuedJson) shouldBe NodeStatus.Queued(results)
    }

    "transform Running to Json" in {
      toJs(running) shouldBe runningJson
    }

    "read Running from Json" in {
      fromJs(runningJson) shouldBe running
    }

    "transform Completed to Json" in {
      toJs(completed) shouldBe completedJson
    }

    "read Completed from Json" in {
      fromJs(completedJson) shouldBe completed
    }

    "transform Failed to Json" in {
      toJs(failed) shouldBe failedJson
    }

    "read Failed from Json" in {
      fromJs(failedJson) shouldBe failed
    }

    "transform Aborted to Json" in {
      toJs(NodeStatus.Aborted(results)) shouldBe abortedJson
    }

    "read Aborted from Json" in {
      fromJs(abortedJson) shouldBe NodeStatus.Aborted(results)
    }
  }

  def fromJs(queuedJson: Json) = queuedJson.as[NodeStatus]
  def toJs(state: NodeStatus) = state.asJson

  def js(state: String, fields: (String, Json)*) = {
    val emptyMap = Seq(
      NodeStatusJsonProtocol.Status,
      NodeStatusJsonProtocol.Started,
      NodeStatusJsonProtocol.Ended,
      NodeStatusJsonProtocol.Results,
      NodeStatusJsonProtocol.Error
    ).map(key => key -> None).toMap[String, Option[Json]]

    val jsFields = (emptyMap ++ fields.toMap.mapValues(Some(_)) +
      (NodeStatusJsonProtocol.Status -> Some("state)))".mapValues {
      case None    => Json.Null
      case Some(v) => v
    }
    Json(jsFields)
  }
}