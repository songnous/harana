package com.harana.sdk.backend.models.flow

import com.harana.sdk.backend.models.flow.Catalog.ActionObjectCatalog
import com.harana.sdk.backend.models.flow.actionobjects.ActionObjectInfoMock
import com.harana.sdk.backend.models.flow.inference.{InferContext, InferenceWarnings}
import com.harana.sdk.backend.models.flow.actionobjects.ActionObjectInfoMock
import com.harana.sdk.backend.models.flow.inference.{InferContext, InferenceWarnings}
import com.harana.sdk.shared.models.flow.{ActionInfo, ActionObjectInfo}
import com.harana.sdk.shared.models.flow.parameters.Parameter
import com.harana.sdk.shared.models.flow.parameters.validators.RangeValidator
import com.harana.sdk.shared.models.flow.utils.Id
import org.scalatest.funsuite.AnyFunSuite

import scala.reflect.runtime.{universe => ru}

object DClassesForActions {
  trait A extends ActionObjectInfoMock
  case class A1() extends A {
    val id = "test"
  }
  case class A2() extends A {
    val id = "test"
  }
}

object ActionForPortTypes {
  import DClassesForActions._

  class SimpleAction extends Action1To1[A1, A2] {

    def execute(t0: A1)(context: ExecutionContext): A2 = ???

    val id: Id = ActionInfo.Id.randomId
    val name = ""
    val description = ""
    val parameters = Array.empty[Parameter[_]]

    lazy val tTagTI_0: ru.TypeTag[A1] = ru.typeTag[A1]
    lazy val tTagTO_0: ru.TypeTag[A2] = ru.typeTag[A2]
  }
}

class ActionSuite extends AnyFunSuite with TestSupport {

  test("It is possible to implement simple actions") {
    import DClassesForActions._

    class PickOne extends Action2To1[A1, A2, A] {
      val id: Id = ActionInfo.Id.randomId

      val param = NumericParameter("param", None, RangeValidator.allInt)
      def setParam(int: Int): this.type = set(param -> int)
      val parameters =  Array(param)

      def execute(t1: A1, t2: A2)(context: ExecutionContext): A = if ($(param) % 2 == 1) t1 else t2
      val name = "Some name"
      val description = "Some description"

      lazy val tTagTI_0: ru.TypeTag[A1] = ru.typeTag[A1]
      lazy val tTagTO_0: ru.TypeTag[A]  = ru.typeTag[A]
      lazy val tTagTI_1: ru.TypeTag[A2] = ru.typeTag[A2]
    }

    val firstPicker  = new PickOne
    firstPicker.setParam(1)
    val secondPicker = new PickOne
    secondPicker.setParam(2)

    val input = Vector(A1(), A2())
    assert(firstPicker.executeUntyped(input)(mock[ExecutionContext]) == Vector(A1()))
    assert(secondPicker.executeUntyped(input)(mock[ExecutionContext]) == Vector(A2()))

    val h = new ActionObjectCatalog
    h.register[A1]
    h.register[A2]
    val context = createInferContext(h)

    val knowledge = Vector[Knowledge[ActionObjectInfo]](Knowledge(A1()), Knowledge(A2()))
    val (result, warnings) = firstPicker.inferKnowledgeUntyped(knowledge)(context)
    assert(result == Vector(Knowledge(A1(), A2())))
    assert(warnings == InferenceWarnings.empty)
  }

  test("It is possible to override knowledge inferring in Action") {
    import DClassesForActions._

    val mockedWarnings = mock[InferenceWarnings]

    class GeneratorOfA extends Action0To1[A] {
      val id = ActionInfo.Id.randomId

      def execute()(context: ExecutionContext): A                                             = ???
      override def inferKnowledge()(context: InferContext): (Knowledge[A], InferenceWarnings) = (Knowledge(A1(), A2()), mockedWarnings)

      val name = ""
      val description = ""
      val parameters = Array.empty[Parameter[_]]
      lazy val tTagTO_0: ru.TypeTag[A] = ru.typeTag[A]
    }

    val generator: Action = new GeneratorOfA

    val h = new ActionObjectCatalog
    h.register[A1]()
    h.register[A2]()
    val context = createInferContext(h)

    val (results, warnings) = generator.inferKnowledgeUntyped(Vector())(context)
    assert(results == Vector(Knowledge(A1(), A2())))
    assert(warnings == mockedWarnings)
  }

  test("Getting types required in input port") {
    import ActionForPortTypes._
    val op = new SimpleAction
    assert(op.inPortTypes == Vector(ru.typeTag[DClassesForActions.A1]))
  }

  test("Getting types required in output port") {
    import ActionForPortTypes._
    val op = new SimpleAction
    assert(op.outPortTypes == Vector(ru.typeTag[DClassesForActions.A2]))
  }
}