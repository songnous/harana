package com.harana.workflowexecutor

import com.harana.sdk.backend.models.flow.actions.CreateCustomTransformer
import com.harana.sdk.backend.models.flow.actions.custom.{Sink, Source}
import com.harana.sdk.backend.models.flow.graph.{AbstractInferenceSpec, GraphKnowledge}
import com.harana.sdk.backend.models.flow.inference.exceptions.NoInputEdgesError
import com.harana.sdk.backend.models.flow.{Action, MockedInferContext}
import com.harana.sdk.shared.models.designer.flow.graph.FlowGraph
import com.harana.sdk.shared.models.designer.flow.graph.node.Node
import com.harana.sdk.shared.models.designer.flow.parameters.custom.InnerWorkflow

class KnowledgeInferenceSpec extends AbstractInferenceSpec {

  "Graph" should {

    "infer knowledge for nested nodes (ex. CreateCustomTransformer)" in {
      val rootWorkflowWithSomeInnerWorkflow = {
        val createCustomTransformer = actionCatalog.createAction(CreateCustomTransformer().id).asInstanceOf[CreateCustomTransformer]
        createCustomTransformer.setInnerWorkflow(CreateCustomTransformer.default)
        FlowGraph(Set(createCustomTransformer.toNode))
      }

      val inferenceResult = rootWorkflowWithSomeInnerWorkflow.inferKnowledge(inferContext, GraphKnowledge())

      // There is 1 node in root workflow and two more in inner workflow.
      inferenceResult.results.size should be > 1
    }
  }

  "Node errors" should {

    "be properly inferred for inner workflow. For example" when {

      "sink node has no input connected " in {
        val sinkExpectedToHaveErrors = actionCatalog.createAction(new Sink().id).toNode
        val rootWorkflowWithInvalidInnerWorkflow = {
          val createCustomTransformer = actionCatalog.createAction(CreateCustomTransformer().id).asInstanceOf[CreateCustomTransformer]

          val innerWorkflow = {
            val source = actionCatalog.createAction(new Source().id).toNode
            val graph  = FlowGraph(Set(source, sinkExpectedToHaveErrors), Set.empty)
            InnerWorkflow(graph, Json.Null, List.empty)
          }

          createCustomTransformer.setInnerWorkflow(innerWorkflow)
          FlowGraph(Set(createCustomTransformer.toNode), Set.empty)
        }

        val inferenceResult = rootWorkflowWithInvalidInnerWorkflow.inferKnowledge(inferContext, GraphKnowledge())
        inferenceResult.errors(sinkExpectedToHaveErrors.id).head should matchPattern { case NoInputEdgesError(0) => }
      }
    }
  }

  implicit class ActionTestExtension(val action: Action) {
    def toNode: Node[Action] = Node(Node.Id.randomId, action)
  }

  private lazy val inferContext = MockedInferContext(actionObjectCatalog = actionObjectCatalog)

  private lazy val FlowCatalog(_, actionObjectCatalog, actionCatalog) = CatalogRecorder.resourcesCatalogRecorder.catalogs

}
