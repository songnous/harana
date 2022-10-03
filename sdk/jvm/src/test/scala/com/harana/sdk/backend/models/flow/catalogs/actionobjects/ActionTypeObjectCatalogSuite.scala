package com.harana.sdk.backend.models.flow.catalogs.actionobjects

import com.harana.sdk.backend.models.flow.actionobjects.ActionObjectInfoMock
import com.harana.sdk.backend.models.flow.catalogs.actionobjects.Constructors.{AuxiliaryParameterless, NotParameterLess, WithDefault}
import com.harana.sdk.backend.models.flow.catalogs.actionobjects.MixinInheritance.{OpA, OpB, P}
import com.harana.sdk.backend.models.flow.actionobjects.ActionObjectInfoMock
import com.harana.sdk.backend.models.flow.utils.catalog.exceptions.NoParameterlessConstructorInClassError
import com.harana.sdk.shared.models.designer.flow.utils.catalog.exceptions.NoParameterlessConstructorInClassError
import com.harana.sdk.shared.models.designer.flow.utils.catalog.{ClassDescriptor, TraitDescriptor}
import com.harana.sdk.shared.models.designer.flow.ActionObjectCatalog
import com.harana.sdk.shared.models.flow.actionobjects.ActionObjectInfo
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

import scala.reflect.runtime.{universe => ru}

object SampleInheritance {
  trait T1 extends ActionObjectInfo
  trait T2 extends T1
  trait T3 extends T1
  trait T extends ActionObjectInfo

  abstract class A extends T3
  case class B() extends A with T
  case class C() extends A with T2
}

object Parametrized {
  trait T[T] extends ActionObjectInfoMock
  abstract class A[T] extends ActionObjectInfoMock
  class B extends A[Int]

}

object Constructors {

  class NotParameterLess(val i: Int) extends ActionObjectInfoMock

  class AuxiliaryParameterless(val i: Int) extends ActionObjectInfoMock {
    def this() = this(1)
  }

  class WithDefault(val i: Int = 1) extends ActionObjectInfoMock

}

object TraitInheritance {
  class C1 extends ActionObjectInfoMock
  trait T1 extends C1
  trait T2 extends T1
  class C2 extends T2
  trait S1 extends ActionObjectInfo
  trait S2 extends ActionObjectInfo
  class A1 extends ActionObjectInfoMock
  trait S3 extends A1 with S1 with S2
}

object MixinInheritance {
  trait P extends ActionObjectInfo
  trait TrA extends ActionObjectInfo
  trait TrB extends ActionObjectInfo
  class OpC extends ActionObjectInfo
  class OpA extends TrA with P
  class OpB extends TrB with P
}

class ActionTypeObjectCatalogSuite extends AnyFunSuite with Matchers {

  def testGettingSubclasses[T <: ActionObjectInfo: ru.TypeTag](h: ActionObjectCatalog, expected: ActionObjectInfo*) =
    h.concreteSubclassesInstances[T] should contain theSameElementsAs expected

  test("Getting concrete subclasses instances") {
    import SampleInheritance._

    val h = new ActionObjectCatalog
    h.register[B]
    h.register[C]

    val b = new B
    val c = new C

    def check[T <: ActionObjectInfo: ru.TypeTag](expected: ActionObjectInfo*) =
      testGettingSubclasses[T](h, expected: _*)

    check[T with T1](b)
    check[T2 with T3](c)
    check[B](b)
    check[C](c)
    check[A with T2](c)
    check[A with T1](b, c)
    check[A](b, c)
    check[T3](b, c)
    check[T with T2]()
  }

  test("Getting concrete subclasses instances using ru.TypeTag") {
    import SampleInheritance._
    val h = new ActionObjectCatalog
    h.register[B]
    val t = ru.typeTag[T]
    h.concreteSubclassesInstances(t) should contain theSameElementsAs List(new B)
  }

  test("Listing DTraits and DClasses") {
    import SampleInheritance._
    val h = new ActionObjectCatalog
    h.register[B]
    h.register[C]

    def name[T: ru.TypeTag] = ru.typeOf[T].typeSymbol.fullName

    val traits = (TraitDescriptor(name[ActionObjectInfo], Nil) ::
      TraitDescriptor(name[T2], List(name[T1])) ::
      TraitDescriptor(name[T], List(name[ActionObjectInfo])) ::
      TraitDescriptor(name[T1], List(name[ActionObjectInfo])) ::
      TraitDescriptor(name[T3], List(name[T1])) ::
      Nil).map(t => t.name -> t).toMap

    val classes = (ClassDescriptor(name[A], None, List(name[T3])) ::
      ClassDescriptor(name[B], Some(name[A]), List(name[T])) ::
      ClassDescriptor(name[C], Some(name[A]), List(name[T2])) ::
      Nil).map(c => c.name -> c).toMap

    val descriptor = h.descriptor
    descriptor.traits should contain theSameElementsAs traits
    descriptor.classes should contain theSameElementsAs classes
  }

  test("Registering class extending parametrized class") {
    val p = new ActionObjectCatalog
    p.register[B]()
  }

  test("Registering parametrized class") {
    val p = new ActionObjectCatalog
    p.register[A[Int]]()
  }

  test("Registering parametrized trait") {
    val p = new ActionObjectCatalog
    p.register[T[Int]]()
  }

  test("Registering concrete class with no parameter-less constructor should produce exception") {
    intercept[NoParameterlessConstructorInClassError] {
      val h = new ActionObjectCatalog
      h.register[NotParameterLess]
    }
  }

  test("Registering class with constructor with default parameters should produce exception") {
    intercept[NoParameterlessConstructorInClassError] {
      val h = new ActionObjectCatalog
      h.register[WithDefault]
    }
  }

  test("Registering class with auxiliary parameterless constructor should succeed") {
    val h = new ActionObjectCatalog
    h.register[AuxiliaryParameterless]
  }

  test("It is possible to register a trait that extends a class") {
    val h = new ActionObjectCatalog
    h.register[OpB]
    h.register[OpA]
    val subclasses = h.concreteSubclassesInstances[P]
    assert(subclasses.size == 2)
    assert(subclasses.exists(x => x.isInstanceOf[OpA]))
    assert(subclasses.exists(x => x.isInstanceOf[OpB]))
  }
}
