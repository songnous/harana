package com.harana.sdk.backend.models.flow.json.workflow

import scala.util.Success
import org.mockito.Mockito._
import org.mockito.ArgumentMatchers.any
import com.harana.sdk.backend.models.flow.utils.Logging
import com.harana.sdk.shared.models.common.Version
import com.harana.sdk.backend.models.flow.json.{StandardSpec, UnitTestSupport}
import com.harana.sdk.backend.models.flow.utils.Logging
import com.harana.sdk.shared.models.designer.flow.flows._
import com.harana.sdk.shared.models.flow.{EntitiesMap, ExecutionReport, FlowType}
import com.harana.sdk.shared.models.flow.exceptions.WorkflowVersionFormatError
import com.harana.sdk.shared.models.flow.graph.FlowGraph
import io.circe.Json
import io.circe.syntax.EncoderOps

class WorkflowVersionUtilSpec extends StandardSpec with UnitTestSupport with Logging {

  val currentVersionString = "1.2.3"
  def currentVersion = Version(currentVersionString)


  val correctWorkflow = Workflow(FlowType.Batch, currentVersionString, FlowGraph(), Json.Null)
  val incorrectVersion = Map("metadata" -> Map("apiVersion" -> Json.fromString("FOOBAR")), "foo" -> Json.fromString("bar"))

  val workflowId = Workflow.Id.randomId

  val workflowWithResults = WorkflowWithResults(
    workflowId,
    FlowType.Batch,
    currentVersionString,
    FlowGraph(),
    Json.Null,
    ExecutionReport(Map(), EntitiesMap(), None),
    WorkflowInfo.forId(workflowId)
  )

  "WorkflowVersionUtil" should {
    "allow to extract the version as a string and as an object" in {
      val versionString = "3.2.1"
      val okJson = Json("metadata" -> Json("apiVersion" -> versionString))
      extractVersion(okJson) shouldBe Success(versionString)
      extractVersion(okJson.compactPrint) shouldBe Success(Version(versionString))

      val wrongJson = Json("metadataFOO" -> Json("apiVersion" -> versionString))
      extractVersion(wrongJson) shouldBe Symbol(Failure)
      extractVersion(wrongJson.compactPrint) shouldBe Symbol(Failure)
    }

    "parse a Workflow and return an object or a string if version is invalid" in {
      workflowOrString(correctWorkflow.asJson) shouldBe Right(correctWorkflow)
      workflowOrString(incorrectVersion.asJson) shouldBe Left(incorrectVersionJsonString)
    }

    "expose a JsonReader for Workflow that checks the version" in {
      correctWorkflowString.parseJson.as[Workflow](versionedWorkflowReader) shouldBe correctWorkflow
      an[WorkflowVersionFormatError] shouldBe thrownBy(incorrectVersionJsonString.parseJson.as[Workflow](versionedWorkflowReader))
    }

    "expose a JsonReader for WorkflowWithResults that checks the version" in {
      workflowWithResultsString.parseJson.as[WorkflowWithResults](versionedWorkflowWithResultsReader) shouldBe workflowWithResults
      an[WorkflowVersionFormatError] shouldBe thrownBy(incorrectVersionJsonString.parseJson.as[WorkflowWithResults](versionedWorkflowWithResultsReader))
    }
  }
}
