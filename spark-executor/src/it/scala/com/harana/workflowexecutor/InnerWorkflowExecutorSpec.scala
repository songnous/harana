package com.harana.workflowexecutor

import com.harana.sdk.backend.models.flow._
import com.harana.sdk.backend.models.flow.actionobjects.SqlColumnTransformer
import com.harana.sdk.backend.models.flow.actions.SqlColumnTransformation
import com.harana.sdk.backend.models.flow.actions.custom.{Sink, Source}
import com.harana.sdk.backend.models.flow.actions.spark.wrappers.evaluators.CreateRegressionEvaluator
import com.harana.sdk.shared.models.designer
import com.harana.sdk.shared.models.designer.flow.actionobjects.multicolumn.MultiColumnParameters.SingleOrMultiColumnChoices.SingleColumnChoice
import com.harana.sdk.shared.models.designer.flow.actionobjects.multicolumn.SingleColumnParameters.SingleTransformInPlaceChoices.NoInPlaceChoice
import com.harana.sdk.shared.models.designer.flow.exceptions.HaranaError
import com.harana.sdk.shared.models.designer.flow.graph.node.Node
import com.harana.sdk.shared.models.designer.flow.graph.{Edge, FlowGraph}
import com.harana.sdk.shared.models.designer.flow.parameters.selections.NameSingleColumnSelection
import com.harana.workflowexecutor.executor.InnerWorkflowExecutorImpl
import org.apache.spark.sql.Row
import org.apache.spark.sql.types.{DoubleType, StructField, StructType}


class InnerWorkflowExecutorSpec extends IntegratedTestSupport {

  import IntegratedTestSupport._
  import LocalExecutionContext._

  val sourceNodeId = "2603a7b5-aaa9-40ad-9598-23f234ec5c32"
  val sinkNodeId = "d7798d5e-b1c6-4027-873e-a6d653957418"
  val innerNodeId = "b22bd79e-337d-4223-b9ee-84c2526a1b75"
  val sourceNode = Node(sourceNodeId, new Source())
  val sinkNode = Node(sinkNodeId, new Sink())

  def innerNodeAction = {
    val inPlace = NoInPlaceChoice().setOutputColumn("output")
    val single = SingleColumnChoice().setInputColumn(NameSingleColumnSelection("column1")).setInPlaceChoice(inPlace)
    val params = new SqlColumnTransformer().setFormula("2*x").setSingleOrMultiChoice(single).parameterValuesToJson
    new SqlColumnTransformation().setParametersFromJson(params)
  }

  def failingAction = {
    val inPlace = NoInPlaceChoice().setOutputColumn("output")
    val single = SingleColumnChoice().setInputColumn(NameSingleColumnSelection("does not exist")).setInPlaceChoice(inPlace)
    val params = new SqlColumnTransformer().setFormula("2*x").setSingleOrMultiChoice(single).parameterValuesToJson
    new SqlColumnTransformation().setParametersFromJson(params)
  }

  val innerNode = Node(innerNodeId, innerNodeAction)
  val failingNode = Node(innerNodeId, failingAction)
  val otherNode = Node(Node.Id.randomId, new CreateRegressionEvaluator())

  val simpleGraph = FlowGraph(
    Set(sourceNode, sinkNode, innerNode),
    Set(Edge(sourceNode, 0, innerNode, 0), Edge(innerNode, 0, sinkNode, 0))
  )

  val disconnectedGraph = FlowGraph(Set(sourceNode, sinkNode, innerNode), Set(Edge(sourceNode, 0, innerNode, 0)))

  val cyclicGraph = FlowGraph(
    Set(sourceNode, sinkNode, innerNode),
    Set(Edge(sourceNode, 0, sinkNode, 0), Edge(innerNode, 0, innerNode, 0))
  )

  val failingGraph = FlowGraph(
    Set(sourceNode, sinkNode, failingNode),
    Set(Edge(sourceNode, 0, failingNode, 0), Edge(failingNode, 0, sinkNode, 0))
  )

  val otherGraph = FlowGraph(Set(sourceNode, sinkNode, otherNode), Set(Edge(sourceNode, 0, sinkNode, 0)))
  val actionsCatalog = CatalogRecorder.resourcesCatalogRecorder.catalogs.actions
  val executor = new InnerWorkflowExecutorImpl
  val schema = StructType(List(StructField("column1", DoubleType), StructField("column2", DoubleType)))

  val rows = Seq(
    Row(1.0, 2.0),
    Row(2.0, 3.0)
  )

  val df = createDataFrame(rows, schema)

  "InnerWorkflowExecutor" should {

    "parse inner workflow json" in {
      val innerWorkflow = designer.flow.parameters.custom.InnerWorkflow(simpleGraph, Json())
      executor.parse(innerWorkflow.asJson.asJson) shouldBe innerWorkflow
    }

    "execute workflow" in {
      val expectedSchema = StructType(List(StructField("column1", DoubleType), StructField("column2", DoubleType), StructField("output", DoubleType)))
      val expectedRows = Seq(Row(1.0, 2.0, 2.0), Row(2.0, 3.0, 4.0))
      val expected = createDataFrame(expectedRows, expectedSchema)
      val innerWorkflow = designer.flow.parameters.custom.InnerWorkflow(simpleGraph, Json())
      val transformed = executor.execute(commonExecutionContext, innerWorkflow, df)
      assertDataFramesEqual(transformed, expected)
    }

    "execute workflow with more ready nodes" in {
      val innerWorkflow = designer.flow.parameters.custom.InnerWorkflow(otherGraph, Json())
      val transformed = executor.execute(commonExecutionContext, innerWorkflow, df)
      assertDataFramesEqual(transformed, df)
    }

    "throw an exception" when {

      "parsing json that is not workflow" in {
        an[Exception] should be thrownBy {
          executor.parse(Json("this format is" -> JsString("invalid")))
        }
      }

      "workflow contains cycle" in {
        val innerWorkflow = designer.flow.parameters.custom.InnerWorkflow(cyclicGraph, Json())
        a[HaranaError] should be thrownBy {
          executor.execute(commonExecutionContext, innerWorkflow, df)
        }
      }

      "workflow is not connected" in {
        val innerWorkflow = designer.flow.parameters.custom.InnerWorkflow(disconnectedGraph, Json())
        a[HaranaError] should be thrownBy {
          executor.execute(commonExecutionContext, innerWorkflow, df)
        }
      }

      "workflow execution fails" in {
        val innerWorkflow = designer.flow.parameters.custom.InnerWorkflow(failingGraph, Json())
        a[HaranaError] should be thrownBy {
          executor.execute(commonExecutionContext, innerWorkflow, df)
        }
      }
    }
  }

  val workflowJson =
    """{
      |  "workflow": {
      |    "nodes": [
      |      {
      |        "id": "2603a7b5-aaa9-40ad-9598-23f234ec5c32",
      |        "action": {
      |          "id": "f94b04d7-ec34-42f7-8100-93fe235c89f8",
      |          "name": "Source"
      |        },
      |        "parameters": {}
      |      }, {
      |        "id": "d7798d5e-b1c6-4027-873e-a6d653957418",
      |        "action": {
      |          "id": "e652238f-7415-4da6-95c6-ee33808561b2",
      |          "name": "Sink"
      |        },
      |        "parameters": {}
      |      }, {
      |        "id": "b22bd79e-337d-4223-b9ee-84c2526a1b75",
      |        "action": {
      |          "id": "012876d9-7a72-47f9-98e4-8ed26db14d6d",
      |          "name": "Execute Mathematical Transformation"
      |        },
      |        "parameters": {
      |          "input column alias": "x",
      |          "formula": "2*x",
      |          "input column": {
      |            "type": "column",
      |            "value": "column1"
      |          },
      |          "output column name": "output"
      |        }
      |      }
      |    ],
      |    "connections": [
      |      {
      |        "from":{
      |          "nodeId": "2603a7b5-aaa9-40ad-9598-23f234ec5c32",
      |          "portIndex": 0
      |        },
      |        "to": {
      |          "nodeId": "b22bd79e-337d-4223-b9ee-84c2526a1b75",
      |          "portIndex":0
      |        }
      |      }, {
      |        "from": {
      |          "nodeId": "b22bd79e-337d-4223-b9ee-84c2526a1b75",
      |          "portIndex":0
      |        },
      |        "to": {
      |          "nodeId": "d7798d5e-b1c6-4027-873e-a6d653957418",
      |          "portIndex":0
      |        }
      |      }
      |    ]
      |  },
      |  "thirdPartyData": "{}",
      |  "source": "2603a7b5-aaa9-40ad-9598-23f234ec5c32",
      |  "sink": "d7798d5e-b1c6-4027-873e-a6d653957418"
      |}""".stripMargin

}
