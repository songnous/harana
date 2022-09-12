package com.harana.sdk.backend.models.flow.json.workflow

import com.harana.sdk.shared.models.designer.flow.utils.catalog.SortPriority
import com.harana.sdk.shared.models.flow.ActionInfo
import com.harana.sdk.shared.models.flow.catalogs.{ActionCategory, ActionCategoryNode, ActionDescriptor}
import io.circe.Json
import io.circe.syntax.EncoderOps
import org.mockito.Mockito._
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import org.scalatestplus.mockito.MockitoSugar

import scala.collection.immutable.SortedMap

class ActionCategoryNodeJsonProtocolSpec extends AnyFlatSpec with Matchers with MockitoSugar {

  "ActionCategoryNode" should "be correctly serialized to json" in {
    val childCategory = new ActionCategory(ActionCategory.Id.randomId, "mock child name", SortPriority.coreDefault) {}
    val childNode = ActionCategoryNode(Some(childCategory))

    val actionDescriptor = mock[ActionDescriptor]
    when(actionDescriptor.id).thenReturn(ActionInfo.Id.randomId)
    when(actionDescriptor.name).thenReturn("mock action descriptor name")
    when(actionDescriptor.description).thenReturn("mock operator descriptor description")

    val node = ActionCategoryNode(
      None,
      successors = SortedMap(childCategory -> childNode),
      actions = List(actionDescriptor)
    )

    val expectedJson = Map(
      "catalog" -> Seq(
                      Map(
                        "id"      -> Json.fromString(childCategory.id.toString),
                        "name"    -> Json.fromString(childCategory.name),
                        "catalog" -> Seq(Json.Null).asJson,
                        "items"   -> Seq(Json.Null).asJson
                      )
                    ).asJson,
      "items"   -> Seq(
                      Map(
                        "id"          -> Json.fromString(actionDescriptor.id.toString),
                        "name"        -> Json.fromString(actionDescriptor.name),
                        "description" -> Json.fromString(actionDescriptor.description)
                      )
                    ).asJson
    )

    node.asJson shouldBe expectedJson.asJson
  }
}