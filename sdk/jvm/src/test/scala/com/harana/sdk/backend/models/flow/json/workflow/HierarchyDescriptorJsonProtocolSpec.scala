package com.harana.sdk.backend.models.flow.json.workflow

import org.scalatest._
import org.scalatest.matchers.should.Matchers
import org.scalatestplus.mockito.MockitoSugar
import org.scalatest.flatspec.AnyFlatSpec
import com.harana.sdk.shared.models.designer.flow.json.workflow.HierarchyDescriptorJsonProtocol
import com.harana.sdk.shared.models.designer.flow.utils.catalog.{ClassDescriptor, HierarchyDescriptor, TraitDescriptor}
import io.circe.Json
import io.circe.syntax.EncoderOps

class HierarchyDescriptorJsonProtocolSpec extends AnyFlatSpec with Matchers with MockitoSugar {

  "TraitDescriptor" should "be correctly serialized to json" in {
    val traitDescriptor = TraitDescriptor("Sorted", List("Iterable", "Comparable"))
    val json = traitDescriptor.asJson
    assert(expectedJsTrait(traitDescriptor) == json)
  }

  it should "be correctly serialized to json when parents are empty" in {
    val traitDescriptor = TraitDescriptor("Sorted", List())
    val json = traitDescriptor.asJson
    assert(expectedJsTrait(traitDescriptor) == json)
  }

  it should "be correctly deserialized from json" in {
    val descriptor = TraitDescriptor("Sorted", List("Iterable", "Comparable"))
    val json = expectedJsTrait(descriptor)
    val fromJson = json.as[TraitDescriptor].toOption.get
    assert(descriptor == fromJson)
  }

  it should "be correctly deserialized from json when parents are empty" in {
    val descriptor = TraitDescriptor("Sorted", List())
    val json = expectedJsTrait(descriptor)
    val fromJson = json.as[TraitDescriptor].toOption.get
    assert(descriptor == fromJson)
  }

  "ClassDescriptor" should "be correctly serialized to json when no parent" in {
    val classDescriptor: ClassDescriptor = ClassDescriptor("Model", None, List("T1", "T2"))
    val json = classDescriptor.asJson
    assert(expectedJsClass(classDescriptor) == json)
  }

  it should "be correctly serialized to json when without traits" in {
    val classDescriptor: ClassDescriptor = ClassDescriptor("Model", Some("parent"), List())
    val json = classDescriptor.asJson
    assert(expectedJsClass(classDescriptor) == json)
  }

  it should "be correctly deserialized from json when no parent" in {
    val descriptor: ClassDescriptor = ClassDescriptor("Model", None, List("T1", "T2"))
    val json = expectedJsClass(descriptor)
    val fromJson = json.as[ClassDescriptor].toOption.get
    assert(descriptor == fromJson)
  }

  it should "be correctly deserialized from json when without traits" in {
    val descriptor: ClassDescriptor = ClassDescriptor("Model", Some("parent"), List())
    val json = expectedJsClass(descriptor)
    val fromJson = json.as[ClassDescriptor].toOption.get
    assert(descriptor == fromJson)
  }

  "HierarchyDescriptor" should "be correctly serialized to json" in {
    val hierarchyDescriptor: HierarchyDescriptor = HierarchyDescriptor(
      Map(
        "Sorted" -> TraitDescriptor("Sorted", List("Iterable", "Comparable")),
        "Trainable" -> TraitDescriptor("Trainable", List())
      ),
      Map(
        "DataFrame" -> ClassDescriptor("DataFrame", Some("Data"), List("Data")),
        "Whatever" -> ClassDescriptor("Whatever", None, List())
      )
    )

    val json = hierarchyDescriptor.asJson
    assert(expectedJsHierarchy(hierarchyDescriptor) == json)
  }

  it should "be correctly serialized to json when empty" in {
    val hierarchyDescriptor: HierarchyDescriptor = HierarchyDescriptor(Map(), Map())
    val json = hierarchyDescriptor.asJson
    assert(expectedJsHierarchy(hierarchyDescriptor) == json)
  }

  it should "be correctly deserialized from json" in {
    val hierarchyDescriptor: HierarchyDescriptor = HierarchyDescriptor(
      Map(
        "Sorted"    -> TraitDescriptor("Sorted", List("Iterable", "Comparable")),
        "Trainable" -> TraitDescriptor("Trainable", List())
      ),
      Map(
        "DataFrame" -> ClassDescriptor("DataFrame", Some("Data"), List("Data")),
        "Whatever"  -> ClassDescriptor("Whatever", None, List())
      )
    )
    val json = hierarchyDescriptor.asJson
    val fromJson = json.as[HierarchyDescriptor].toOption.get
    assert(hierarchyDescriptor == fromJson)
  }

  it should "be correctly deserialized from json when empty" in {
    val hierarchyDescriptor = HierarchyDescriptor(Map(), Map())
    val json = hierarchyDescriptor.asJson
    val fromJson = json.as[HierarchyDescriptor].toOption.get
    assert(hierarchyDescriptor == fromJson)
  }

  private def expectedJsHierarchy(hierarchy: HierarchyDescriptor) =
    Map(
      "traits"  -> hierarchy.traits.values.map(t => t.name -> expectedJsTrait(t)).toMap.asJson,
      "classes" -> hierarchy.classes.values.map(c => c.name -> expectedJsClass(c)).toMap.asJson
    ).asJson

  private def expectedJsTrait(traitDescriptor: TraitDescriptor) =
    Map(
      "name"    -> Json.fromString(traitDescriptor.name),
      "parents" -> traitDescriptor.parents.asJson
    ).asJson

  private def expectedJsClass(classDescriptor: ClassDescriptor) =
    Map(
      "name"    -> Json.fromString(classDescriptor.name),
      "parent"  -> classDescriptor.parent.map(_.asJson).getOrElse(Json.Null),
      "traits"  -> classDescriptor.traits.asJson
    ).asJson
}
