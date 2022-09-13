package com.harana.workflowexecutor

import akka.actor.{ActorSystem, Props}
import akka.http.scaladsl.model.StatusCodes
import akka.parboiled2.util.Base64
import akka.pattern.ask
import akka.util.Timeout
import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock._
import com.github.tomakehurst.wiremock.client.{MappingBuilder, WireMock}
import com.github.tomakehurst.wiremock.core.WireMockConfiguration._
import com.harana.sdk.backend.models.flow.json.StandardSpec
import com.harana.sdk.shared.models.designer.flow.{ExecutionReport, FlowType}
import com.harana.sdk.shared.models.designer.flow.flows._
import com.harana.sdk.shared.models.designer.flow.graph.FlowGraph
import com.harana.workflowexecutor.WorkflowManagerClientActorProtocol.{GetWorkflow, Request, SaveState, SaveWorkflow}
import com.harana.workflowexecutor.exception.UnexpectedHttpResponseException
import org.scalatest.BeforeAndAfterEach
import org.scalatest.concurrent.ScalaFutures

import scala.concurrent.duration._

class WorkflowManagerClientActorSpec extends StandardSpec with ScalaFutures with BeforeAndAfterEach with WorkflowWithResultsJsonProtocol {

  implicit val patience = PatienceConfig(timeout = 5.seconds)

  val wireMockServer = new WireMockServer(wireMockConfig().port(0))
  val httpHost = "localhost"
  var httpPort = 0
  val workflowsApiPrefix = "workflows"
  val reportsApiPrefix   = "reports"
  val workflowId = Workflow.Id.randomId
  val executionReport = ExecutionReport(Map(), None)

  val workflow = WorkflowWithResults(
    workflowId,
    FlowType.Batch,
    "1.0.0",
    FlowGraph(),
    Json.Null,
    ExecutionReport(Map(), None),
    WorkflowInfo.empty()
  )

  override def beforeEach() = {
    wireMockServer.start()
    httpPort = wireMockServer.port()
    WireMock.configureFor(httpHost, httpPort)
  }

  override def afterEach() =
    wireMockServer.stop()

  "WorkflowManagerClientActor" when {

    "requested to get workflow" should {

      "download workflow" in {
        stubFor(
          get(urlEqualTo(s"/$workflowsApiPrefix/$workflowId")).withUserId().withBasicAuth()
            .willReturn(aResponse().withStatus(StatusCodes.OK.intValue).withBody(workflow.asJson.toString))
        )

        val responseFuture = sendRequest(GetWorkflow(workflowId)).mapTo[Option[WorkflowWithResults]]
        whenReady(responseFuture)(response => response shouldBe Some(workflow))
      }

      "return None when workflow does not exist" in {
        stubFor(
          get(urlEqualTo(s"/$workflowsApiPrefix/$workflowId")).withUserId().withBasicAuth()
            .willReturn(aResponse().withStatus(StatusCodes.NotFound.intValue))
        )

        val responseFuture = sendRequest(GetWorkflow(workflowId)).mapTo[Option[WorkflowWithResults]]
        whenReady(responseFuture)(response => response shouldBe None)
      }

      "fail on HTTP error" in {
        stubFor(
          get(urlEqualTo(s"/$workflowsApiPrefix/$workflowId")).withUserId().withBasicAuth()
            .willReturn(aResponse().withStatus(StatusCodes.InternalServerError.intValue))
        )

        val responseFuture = sendRequest(GetWorkflow(workflowId)).mapTo[Option[WorkflowWithResults]]
        whenReady(responseFuture.failed)(exception => exception shouldBe a[UnexpectedHttpResponseException])
      }
    }

    "requested to save workflow with state" should {

      "upload workflow and receive OK" in {
        stubFor(
          put(urlEqualTo(s"/$workflowsApiPrefix/$workflowId")).withUserId().withBasicAuth()
            .willReturn(aResponse().withStatus(StatusCodes.OK.intValue))
        )

        val responseFuture = sendRequest(SaveWorkflow(workflow))
        whenReady(responseFuture)(_ => responseFuture.value.get shouldBe 'success)
      }

      "upload workflow and receive Created" in {
        stubFor(
          put(urlEqualTo(s"/$workflowsApiPrefix/$workflowId")).withUserId().withBasicAuth()
            .willReturn(aResponse().withStatus(StatusCodes.Created.intValue))
        )

        val responseFuture = sendRequest(SaveWorkflow(workflow))
        whenReady(responseFuture)(_ => responseFuture.value.get shouldBe 'success)
      }

      "fail on HTTP error" in {
        stubFor(
          put(urlEqualTo(s"/$workflowsApiPrefix/$workflowId")).withUserId().withBasicAuth()
            .willReturn(aResponse().withStatus(StatusCodes.InternalServerError.intValue))
        )

        val responseFuture = sendRequest(SaveWorkflow(workflow))
        whenReady(responseFuture.failed)(exception => exception shouldBe a[UnexpectedHttpResponseException])
      }
    }

    "requested to save status" should {

      "upload execution report and receive OK" in {
        stubFor(
          put(urlEqualTo(s"/$reportsApiPrefix/$workflowId")).withUserId().withBasicAuth()
            .willReturn(aResponse().withStatus(StatusCodes.OK.intValue))
        )

        val responseFuture = sendRequest(SaveState(workflowId, executionReport))
        whenReady(responseFuture)(_ => responseFuture.value.get shouldBe 'success)
      }

      "upload execution report and receive Created" in {
        stubFor(
          put(urlEqualTo(s"/$reportsApiPrefix/$workflowId")).withUserId().withBasicAuth()
            .willReturn(aResponse().withStatus(StatusCodes.Created.intValue))
        )

        val responseFuture = sendRequest(SaveState(workflowId, executionReport))
        whenReady(responseFuture)(_ => responseFuture.value.get shouldBe 'success)
      }

      "fail on HTTP error" in {
        stubFor(
          put(urlEqualTo(s"/$reportsApiPrefix/$workflowId")).withUserId().withBasicAuth()
            .willReturn(aResponse().withStatus(StatusCodes.InternalServerError.intValue))
        )

        val responseFuture = sendRequest(SaveState(workflowId, executionReport))
        whenReady(responseFuture.failed)(exception => exception shouldBe a[UnexpectedHttpResponseException])
      }
    }
  }

  val HaranaUserIdHeaderName = "X-Harana-UserId"
  val WorkflowOwnerId          = "SomeUserId"
  val WMUsername               = "WMUsername"
  val WMPassword               = "WMPassword"

  implicit class UserIdHeaderAddition(mb: MappingBuilder) {
    def withUserId(): MappingBuilder = mb.withHeader(HaranaUserIdHeaderName, equalTo(WorkflowOwnerId))

    def withBasicAuth(): MappingBuilder = {
      val expectedHeaderValue = Base64.rfc2045.encodeToString(s"$WMUsername:$WMPassword".getBytes, false)
      mb.withHeader("Authorization", equalTo(s"Basic $expectedHeaderValue"))
    }
  }

  private def sendRequest(request: Request) = {
    implicit val system = ActorSystem()
    implicit val timeoutSeconds = Timeout(3.seconds)

    val actorRef = system.actorOf(
      Props(
        new WorkflowManagerClientActor(
          WorkflowOwnerId,
          WMUsername,
          WMPassword,
          s"http://$httpHost:$httpPort",
          workflowsApiPrefix,
          reportsApiPrefix
        )
      )
    )

    actorRef ? request
  }
}