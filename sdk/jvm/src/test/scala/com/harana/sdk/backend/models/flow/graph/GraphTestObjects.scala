package com.harana.sdk.backend.models.flow.graph

import com.harana.sdk.backend.models.flow._
import com.harana.sdk.backend.models.flow.actionobjects.ActionObjectInfoMock
import com.harana.sdk.backend.models.flow.actionobjects.ActionObjectInfoMock
import com.harana.sdk.backend.models.flow.{Action0To1, Action1To0, Action1To1, Action1To2, Action2To1, ExecutionContext}
import com.harana.sdk.shared.models.flow.ActionInfo
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
    val id: ActionInfo.Id = ActionInfo.Id.randomId
    val name = ""
    val description = ""
    val parameters = Array.empty[Parameter[_]]
  }

  case class ActionCreateA1() extends Action0To1[A1] with ActionBaseFields {
    def execute()(context: ExecutionContext): A1 = ???

    @transient
    lazy val tTagTO_0: ru.TypeTag[A1] = ru.typeTag[A1]
  }

  case class ActionReceiveA1() extends Action1To0[A1] with ActionBaseFields {
    def execute(t0: A1)(context: ExecutionContext) = ???

    @transient
    lazy val tTagTI_0: ru.TypeTag[A1] = ru.typeTag[A1]

  }

  case class ActionA1ToA() extends Action1To1[A1, A] with ActionBaseFields {
    def execute(t1: A1)(context: ExecutionContext): A = ???

    @transient
    lazy val tTagTI_0: ru.TypeTag[A1] = ru.typeTag[A1]

    @transient
    lazy val tTagTO_0: ru.TypeTag[A] = ru.typeTag[A]

  }

  case class ActionAToA1A2() extends Action1To2[A, A1, A2] with ActionBaseFields {
    def execute(in: A)(context: ExecutionContext): (A1, A2) = ???

    @transient
    lazy val tTagTI_0: ru.TypeTag[A] = ru.typeTag[A]

    @transient
    lazy val tTagTO_0: ru.TypeTag[A1] = ru.typeTag[A1]

    @transient
    lazy val tTagTO_1: ru.TypeTag[A2] = ru.typeTag[A2]

  }

  case class ActionA1A2ToA() extends Action2To1[A1, A2, A] with ActionBaseFields {
    def execute(t1: A1, t2: A2)(context: ExecutionContext): A = ???

    @transient
    lazy val tTagTI_0: ru.TypeTag[A1] = ru.typeTag[A1]

    @transient
    lazy val tTagTO_0: ru.TypeTag[A] = ru.typeTag[A]

    @transient
    lazy val tTagTI_1: ru.TypeTag[A2] = ru.typeTag[A2]

  }

  case class ActionAToALogging() extends Action1To1[A, A] with ActionBaseFields {
    def execute(t0: A)(context: ExecutionContext): A = ???
    def trace(message: String) = println(message)

    @transient
    lazy val tTagTI_0: ru.TypeTag[A] = ru.typeTag[A]

    @transient
    lazy val tTagTO_0: ru.TypeTag[A] = ru.typeTag[A]

  }
}
