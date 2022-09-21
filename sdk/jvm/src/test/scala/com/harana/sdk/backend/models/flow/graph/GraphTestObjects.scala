package com.harana.sdk.backend.models.flow.graph

import com.harana.sdk.backend.models.flow._
import com.harana.sdk.backend.models.flow.actionobjects.ActionObjectInfoMock
import com.harana.sdk.backend.models.flow.actionobjects.ActionObjectInfoMock
import com.harana.sdk.backend.models.flow.{ActionType0To1, ActionType1To0, ActionType1To1, ActionType1To2, ActionType2To1, ExecutionContext}
import com.harana.sdk.shared.models.flow.ActionTypeInfo
import com.harana.sdk.shared.models.flow.graph.FlowGraph.FlowNode
import com.harana.sdk.shared.models.flow.graph.GraphAction
import com.harana.sdk.shared.models.flow.graph.node.Node
import com.harana.sdk.shared.models.flow.parameters.Parameter
import org.scalatestplus.mockito.MockitoSugar

import scala.reflect.runtime.{universe => ru}

object RandomNodeFactory {
  def randomNode(action: GraphAction): FlowNode = Node(Node.Id.randomId, action)
}

object DClassesForActions extends MockitoSugar {
  trait A extends ActionObjectInfoMock
  case class A1() extends A
  case class A2() extends A
}

object ActionTestClasses {

  import com.harana.sdk.backend.models.flow.graph.DClassesForActions._

  trait ActionBaseFields extends GraphAction {
    val id: ActionTypeInfo.Id = ActionTypeInfo.Id.randomId
    val name = ""
      val parameters = Array.empty[Parameter[_]]
  }

  case class ActionTypeCreateA1() extends ActionType0To1[A1] with ActionBaseFields {
    def execute()(context: ExecutionContext): A1 = ???

    @transient
    lazy val tTagTO_0: ru.TypeTag[A1] = ru.typeTag[A1]
  }

  case class ActionTypeReceiveA1() extends ActionType1To0[A1] with ActionBaseFields {
    def execute(t0: A1)(context: ExecutionContext) = ???

    @transient
    lazy val tTagTI_0: ru.TypeTag[A1] = ru.typeTag[A1]

  }

  case class ActionTypeA1ToA() extends ActionType1To1[A1, A] with ActionBaseFields {
    def execute(t1: A1)(context: ExecutionContext): A = ???

    @transient
    lazy val tTagTI_0: ru.TypeTag[A1] = ru.typeTag[A1]

    @transient
    lazy val tTagTO_0: ru.TypeTag[A] = ru.typeTag[A]

  }

  case class ActionTypeAToA1A2() extends ActionType1To2[A, A1, A2] with ActionBaseFields {
    def execute(in: A)(context: ExecutionContext): (A1, A2) = ???

    @transient
    lazy val tTagTI_0: ru.TypeTag[A] = ru.typeTag[A]

    @transient
    lazy val tTagTO_0: ru.TypeTag[A1] = ru.typeTag[A1]

    @transient
    lazy val tTagTO_1: ru.TypeTag[A2] = ru.typeTag[A2]

  }

  case class ActionTypeA1A2ToA() extends ActionType2To1[A1, A2, A] with ActionBaseFields {
    def execute(t1: A1, t2: A2)(context: ExecutionContext): A = ???

    @transient
    lazy val tTagTI_0: ru.TypeTag[A1] = ru.typeTag[A1]

    @transient
    lazy val tTagTO_0: ru.TypeTag[A] = ru.typeTag[A]

    @transient
    lazy val tTagTI_1: ru.TypeTag[A2] = ru.typeTag[A2]

  }

  case class ActionTypeAToALogging() extends ActionType1To1[A, A] with ActionBaseFields {
    def execute(t0: A)(context: ExecutionContext): A = ???
    def trace(message: String) = println(message)

    @transient
    lazy val tTagTI_0: ru.TypeTag[A] = ru.typeTag[A]

    @transient
    lazy val tTagTO_0: ru.TypeTag[A] = ru.typeTag[A]

  }
}
