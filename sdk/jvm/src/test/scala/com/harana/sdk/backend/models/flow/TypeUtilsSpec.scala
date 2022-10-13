package com.harana.sdk.backend.models.flow

import com.harana.sdk.backend.models.flow.utils.TypeUtils
import izumi.reflect.Tag

class TypeUtilsSpec extends UnitSpec {

  import TypeUtilsSpec._

  "TypeUtils.describeType" should {
    "describe class" in {
      TypeUtils.describeType(typeTag[A].tpe) shouldBe Seq(describedA)
    }
    "describe trait" in {
      TypeUtils.describeType(typeTag[B].tpe) shouldBe Seq(describedB)
    }
    "describe complex type" in {
      TypeUtils.describeType(typeTag[A with B].tpe) shouldBe Seq(describedA, describedB)
    }
    "describe parametrized type" in {
      TypeUtils.describeType(typeTag[C[A]].tpe) shouldBe Seq(describedC)
    }
    "describe complex parametrized type" in {
      TypeUtils.describeType(typeTag[C[A] with B].tpe) shouldBe Seq(describedC, describedB)
    }
  }
}

object TypeUtilsSpec {
  class A
  trait B
  class C[T]

  val describedA = "com.harana.sdk.backend.models.flow.TypeUtilsSpec.A"
  val describedB = "com.harana.sdk.backend.models.flow.TypeUtilsSpec.B"
  val describedC = "com.harana.sdk.backend.models.flow.TypeUtilsSpec.C"
}
