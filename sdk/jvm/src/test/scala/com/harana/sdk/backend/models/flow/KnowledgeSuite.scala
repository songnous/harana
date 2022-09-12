package com.harana.sdk.backend.models.flow

import scala.reflect.runtime.{universe => ru}
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers
import com.harana.sdk.backend.models.designer.flow.actionobjects.ActionObjectInfoMock
import com.harana.sdk.backend.models.flow.actionobjects.ActionObjectInfoMock
import com.harana.sdk.shared.models.flow.ActionObjectInfo

object ClassesForKnowledge {
  trait A extends ActionObjectInfoMock
  trait B extends ActionObjectInfoMock

  case class A1(i: Int) extends A {
    val id = "test"
  }
  case class A2(i: Int) extends A {
    val id = "test"
  }
  case class B1(i: Int) extends B {
    val id = "test"
  }
  case class B2(i: Int) extends B {
    val id = "test"
  }
}

class KnowledgeSuite extends AnyFunSuite with Matchers {

  test("Knowledge[ActionObject] with same content are equal") {
    case class A(i: Int) extends ActionObjectInfoMock {
      val id = "test"
    }
    case class B(i: Int) extends ActionObjectInfoMock {
      val id = "test"
    }

    val knowledge1 = Knowledge(A(1), B(2), A(3))
    val knowledge2 = Knowledge(A(1), A(3), B(2), A(1))
    knowledge1 shouldBe knowledge2
  }

  test("Knowledge[_] objects with same content are equal") {

    def isAOrB(any: Any) = any.isInstanceOf[A] || any.isInstanceOf[B]

    class A extends ActionObjectInfoMock {
      val id = "test"
      override def equals(any: Any) = isAOrB(any)
      override def hashCode = 1234567
    }
    class B extends ActionObjectInfoMock {
      val id = "test"
      override def equals(any: Any) = isAOrB(any)
      override def hashCode = 1234567
    }

    val knowledge1: Knowledge[A] = Knowledge(new A)
    val knowledge2: Knowledge[B] = Knowledge(new B)

    knowledge1 shouldBe knowledge2
  }

  test("Knowledge with different content are not equal") {
    case class A(i: Int) extends ActionObjectInfoMock {
      val id = "test"
    }

    val knowledge1 = Knowledge(A(1))
    val knowledge2 = Knowledge(A(2))
    knowledge1 shouldNot be(knowledge2)
  }

  test("Knowledge can intersect internal knowledge with external types") {
    import ClassesForKnowledge._
    val knowledge = Knowledge(A1(1), A2(2), B1(1), B2(2))
    knowledge.filterTypes(ru.typeOf[A]) shouldBe Knowledge(A1(1), A2(2))
  }

  test("Sum of two knowledges is sum of their types") {
    import ClassesForKnowledge._
    val knowledge1 = Knowledge[A](A1(1), A2(2))
    val knowledge2 = Knowledge[B](B1(1), B2(2))
    val expectedKnowledgeSum = Knowledge[ActionObjectInfo](A1(1), A2(2), B1(1), B2(2))
    val actualKnowledgeSum = knowledge1 ++ knowledge2

    actualKnowledgeSum shouldBe expectedKnowledgeSum
  }

  test("Knowledge can be constructed of iterable of knowledges") {
    import ClassesForKnowledge._
    val knowledge1 = Knowledge[A](A1(1))
    val knowledge2 = Knowledge[A](A2(2))
    val knowledgeSum = Knowledge(A1(1), A2(2))
    Knowledge(Seq(knowledge1, knowledge2)) shouldBe knowledgeSum
  }
}
