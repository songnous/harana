package com.harana.sdk.backend.models.flow.parameters

import com.harana.sdk.backend.models.flow.UnitSpec
import com.harana.sdk.shared.models.flow.parameters.{Parameter, ParameterMap, ParameterPair, ParameterType}
import io.circe.Json

class ParameterMapSpec extends UnitSpec {

  class MockParameter[T] extends Parameter[T] {

    // cannot use mockito, because asInstanceOf[Any] won't work
    val name = "name"
    val description: Option[String] = Some("description")

    val parameterType: ParameterType = mock[ParameterType]

    def valueToJson(value: T) = ???
    def valueFromJson(jsValue: Json): T = ???

    override def replicate(name: String): MockParameter[T] = new MockParameter[T]
  }

  val intParameter = new MockParameter[Int]
  val intValue = 5
  val intParameterPair = ParameterPair(intParameter, intValue)

  val stringParameter = new MockParameter[String]
  val stringValue = "abc"
  val stringParameterPair = ParameterPair(stringParameter, stringValue)

  val paramPairs = Seq(intParameterPair, stringParameterPair)

  "ParamMap" can {

    "be built from ParamPairs" in {
      val paramMap = ParameterMap(paramPairs: _*)
      ParameterMap().put(stringParameterPair, intParameterPair) shouldBe paramMap
      ParameterMap().put(intParameter, intValue).put(stringParameter, stringValue) shouldBe paramMap
    }

    "be merged with other param map" in {
      val map1 = ParameterMap(paramPairs(0))
      val map2 = ParameterMap(paramPairs(1))
      map1 ++ map2 shouldBe ParameterMap(paramPairs: _*)
    }

    "be updated with other param map" in {
      val map1 = ParameterMap(paramPairs(0))
      val map2 = ParameterMap(paramPairs(1))
      map1 ++= map2
      map1 shouldBe ParameterMap(paramPairs: _*)
    }

    "return sequence of included ParamPairs" in {
      val paramMap = ParameterMap(paramPairs: _*)
      paramMap.toSeq should contain theSameElementsAs paramPairs
    }
  }

  private def mapWithIntParameter = ParameterMap(intParameterPair)

  "ParamMap.put" should {
    "update value of param if it is already defined" in {
      val map      = mapWithIntParameter
      val newValue = 7
      map.put(intParameter, newValue)
      map shouldBe ParameterMap().put(intParameter, newValue)
    }
  }
  "ParamMap.get" should {
    "return Some value" when {
      "param has value assigned" in {
        mapWithIntParameter.get(intParameter) shouldBe Some(intValue)
      }
    }
    "return None" when {
      "param has no value assigned" in {
        mapWithIntParameter.get(stringParameter) shouldBe None
      }
    }
  }

  "ParamMap.getOrElse" should {
    "return value" when {
      "param has value assigned" in {
        mapWithIntParameter.getOrElse(intParameter, 7) shouldBe intValue
      }
    }
    "return provided default" when {
      "param has no value assigned" in {
        val default = "xxx"
        mapWithIntParameter.getOrElse(stringParameter, default) shouldBe default
      }
    }
  }

  "ParamMap.apply" should {
    "return value" when {
      "param has value assigned" in {
        mapWithIntParameter(intParameter) shouldBe intValue
      }
    }
    "throw an Exception" when {
      "param has no value assigned" in {
        a[NoSuchElementException] shouldBe thrownBy {
          mapWithIntParameter(stringParameter)
        }
      }
    }
  }

  "ParamMap.contains" should {
    "return true" when {
      "param has value assigned" in {
        mapWithIntParameter.contains(intParameter) shouldBe true
      }
    }
    "return false" when {
      "param has no value assigned" in {
        mapWithIntParameter.contains(stringParameter) shouldBe false
      }
    }
  }

  "ParamMap.remove" should {
    "remove value and return it" when {
      "param has value assigned" in {
        val map = mapWithIntParameter
        map.remove(intParameter) shouldBe Some(intValue)
        map shouldBe ParameterMap.empty
      }
    }
    "return None" when {
      "param has no value assigned" in {
        mapWithIntParameter.remove(stringParameter) shouldBe None
      }
    }
  }
}
