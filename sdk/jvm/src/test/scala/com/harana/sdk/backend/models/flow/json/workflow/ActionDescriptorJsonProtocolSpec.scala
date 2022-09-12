package com.harana.sdk.backend.models.flow.json.workflow

import com.harana.sdk.shared.models.designer.flow.utils.catalog.SortPriority
import com.harana.sdk.shared.models.flow.catalogs.{ActionCategory, ActionDescriptor}
import com.harana.sdk.shared.models.flow.{ActionInfo, PortPosition}
import com.harana.sdk.shared.models.flow.parameters.Parameters
import io.circe.Json
import io.circe.syntax.EncoderOps
import org.mockito.Mockito._
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import org.scalatestplus.mockito.MockitoSugar

import scala.reflect.runtime.universe.{TypeTag, typeOf}

object HelperTypes {
  class A
  class BO
  trait T1
  trait T2
}

class ActionDescriptorJsonProtocolSpec extends AnyFlatSpec with MockitoSugar with Matchers {

  "ActionDescriptor" should "be correctly serialized to json" in {
    val (actionDescriptor, expectedJson) = actionDescriptorWithExpectedJsRepresentation
    actionDescriptor.asJson shouldBe expectedJson
  }

  it should "be correctly serialized to json omitting its parameters" in {
    val (actionDescriptor, expectedJson) = actionDescriptorWithExpectedJsRepresentation
    val jsonWithoutParameters = Json(expectedJson.asJson.fields - "parameters")
    actionDescriptor.asJson shouldBe jsonWithoutParameters
  }

  def actionDescriptorWithExpectedJsRepresentation: (ActionDescriptor, Json) = {

    import HelperTypes._

    val category = mock[ActionCategory]
    when(category.id).thenReturn(ActionCategory.Id.randomId)

    val parameters = mock[Parameters]
    val parametersJsRepresentation = "Mock parameters representation"
    when(parameters.parametersToJson).thenReturn(parametersJsRepresentation)

    val actionDescriptor = ActionDescriptor(
      ActionInfo.Id.randomId,
      "action name",
      "action description",
      category,
      SortPriority.coreDefault,
      hasDocumentation = false,
      parameters.parametersToJson,
      Seq(typeOf[A], typeOf[A with T1]),
      Vector(PortPosition.Left, PortPosition.Center),
      Seq(typeOf[B], typeOf[B with T2]),
      Vector(PortPosition.Right, PortPosition.Center)
    )

    def name[T: TypeTag] = typeOf[T].typeSymbol.fullName

    val expectedJson = Map(
      "id"               -> Json.fromString(actionDescriptor.id.toString),
      "name"             -> Json.fromString(actionDescriptor.name),
      "category"         -> Json.fromString(category.id.toString),
      "description"      -> Json.fromString(actionDescriptor.description),
      "deterministic"    -> Json.False,
      "hasDocumentation" -> Json.False,
      "parameters"       -> Json.fromString(parametersJsRepresentation),
      "ports"            -> Map(
        "input"  -> Seq(
          Map(
            "portIndex"     -> Json.fromInt(0),
            "required"      -> Json.True,
            "typeQualifier" -> Seq(name[A]),
            "portPosition"  -> Json.fromString("left")
          ),
          Map(
            "portIndex"     -> Json.fromInt(1),
            "required"      -> true,
            "typeQualifier" -> Seq(name[A]), name[T1],
            "portPosition"  -> Json.fromString("center")
          )
        ).asJson,
        "output" -> Seq(
          Map(
            "portIndex"     -> Json.fromInt(0),
            "typeQualifier" -> Seq(name[B]),
            "portPosition"  -> Json.fromString("right")
          ),
          Map(
            "portIndex"     -> Json.fromInt(1),
            "typeQualifier" -> Seq(name[B]), name[T2],
            "portPosition"  -> Json.fromString("center")
          )
        ).asJson
      )
    )

    (actionDescriptor, expectedJson.asJson)
  }
}
