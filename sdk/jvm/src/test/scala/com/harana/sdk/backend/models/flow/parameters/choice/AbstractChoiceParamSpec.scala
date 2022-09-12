package com.harana.sdk.backend.models.flow.parameters.choice

import com.harana.sdk.backend.models.designer.flow.parameters.AbstractParameterSpec
import com.harana.sdk.shared.models.flow.parameters.Parameter

import scala.reflect.runtime.universe._
import com.harana.sdk.shared.models.flow.parameters.choice.Choice
import com.harana.sdk.shared.models.flow.parameters.exceptions.NoArgumentConstructorRequiredError
import io.circe.Json
import io.circe.syntax.EncoderOps

abstract class AbstractChoiceParamSpec[T, U <: Parameter[T]] extends AbstractParameterSpec[T, U] {

  def createChoiceParam[V <: Choice: TypeTag](name: String, description: String): Parameter[V]

  className should {
    "throw an exception when choices don't have no-arg constructor" in {
      a[NoArgumentConstructorRequiredError] should be thrownBy
        createChoiceParam[BaseChoice]("name", "description")
    }
    "throw an exception when unsupported choice is given" in {
      a[DeserializationException] should be thrownBy
        createChoiceParam[ChoiceABC]("name", "description").valueFromJson(Map("unsupportedClass" -> Json.Null).asJson)
    }
    "throw an exception when not all choices are declared" in {
      an[IllegalArgumentException] should be thrownBy createChoiceParam[ChoiceWithoutDeclaration]("name", "description")
    }
  }
}
