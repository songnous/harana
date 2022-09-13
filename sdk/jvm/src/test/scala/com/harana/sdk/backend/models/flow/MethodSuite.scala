package com.harana.sdk.backend.models.flow

import com.harana.sdk.backend.models.flow.Catalog.ActionObjectCatalog
import org.scalatest.funsuite.AnyFunSuite
import com.harana.sdk.backend.models.flow.actionobjects.ActionObjectInfoMock
import com.harana.sdk.backend.models.flow.inference.{InferContext, InferenceWarnings}
import com.harana.sdk.backend.models.flow.actionobjects.ActionObjectInfoMock
import com.harana.sdk.backend.models.flow.inference.{InferContext, InferenceWarnings}

object ClassesForMethods {
  class S extends ActionObjectInfoMock {
    val id = "test"
  }
  case class A(i: Int) extends S { def this() = this(0) }
  case class B(i: Int) extends S { def this() = this(0) }
}

class MethodSuite extends AnyFunSuite with TestSupport {

  test("It is possible to implement class having method") {
    import ClassesForMethods._

    class C extends ActionObjectInfoMock {
      val id = "test"
      val f: Method1To1[Int, A, B] = new Method1To1[Int, A, B] {
        def apply(context: ExecutionContext)(parameters: Int)(t0: A): B =
          B(t0.i + parameters)
      }
    }

    val c = new C
    assert(c.f(mock[ExecutionContext])(2)(A(3)) == B(5))

    val h = new ActionObjectCatalog
    h.register[A]
    h.register[B]

    val context = createInferContext(h)
    val (result, warnings) = c.f.infer(context)(2)(Knowledge(new A()))
    assert(result == Knowledge(new B()))
    assert(warnings == InferenceWarnings.empty)
  }

  test("It is possible to override inferring in method") {
    import ClassesForMethods._

    val mockedWarnings = mock[InferenceWarnings]

    class C extends ActionObjectInfoMock {
      val id = "test"

      val f: Method0To1[Int, S] = new Method0To1[Int, S] {
        override def apply(context: ExecutionContext)(parameters: Int)(): S = A(parameters)
        override def infer(context: InferContext)(parameters: Int)(): (Knowledge[S], InferenceWarnings) = (Knowledge(new A), mockedWarnings)
      }
    }

    val c = new C

    val h = new ActionObjectCatalog
    h.register[A]
    h.register[B]

    val context = createInferContext(h)
    val (result, warnings) = c.f.infer(context)(2)()
    assert(result == Knowledge(new A()))
    assert(warnings == mockedWarnings)
  }
}
