package com.harana.sdk.backend.models.flow.catalogs.actions

import com.harana.sdk.backend.models.flow.Catalog.ActionCatalog
import com.harana.sdk.backend.models.flow._
import com.harana.sdk.backend.models.flow.actionobjects.ActionObjectInfoMock
import com.harana.sdk.backend.models.flow.actiontypes.{ActionType, UnknownActionType}
import com.harana.sdk.backend.models.flow.inference.{InferContext, InferenceWarnings}
import com.harana.sdk.backend.models.flow.actionobjects.ActionObjectInfoMock
import com.harana.sdk.backend.models.flow.{ExecutionContext, Knowledge}
import com.harana.sdk.backend.models.flow.inference.{InferContext, InferenceWarnings}
import com.harana.sdk.shared
import com.harana.sdk.shared.models.designer.flow
import com.harana.sdk.shared.models.flow.ActionTypeInfo
import com.harana.sdk.shared.models.flow.actionobjects.ActionObjectInfo
import com.harana.sdk.shared.models.flow.catalogs.{ActionCategory, ActionCategoryNode, ActionDescriptor}
import com.harana.sdk.shared.models.flow.exceptions.ActionNotFoundError
import com.harana.sdk.shared.models.flow.parameters.Parameter
import com.harana.sdk.shared.models.flow.utils.SortPriority
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers
import org.scalatestplus.mockito.MockitoSugar

import scala.reflect.runtime.universe.{TypeTag, typeTag}

object ActionCatalogTestResources {

  object CategoryTree {

    object IO extends ActionCategory(ActionCategory.Id.randomId, "Input/Output", SortPriority.coreDefault)
    object DataManipulation extends ActionCategory(ActionCategory.Id.randomId, "Data manipulation", IO.priority.nextCore)
    object ML extends ActionCategory(ActionCategory.Id.randomId, "Machine learning", DataManipulation.priority.nextCore) {
      object Classification extends ActionCategory(ActionCategory.Id.randomId, "Classification", ML.priority.nextCore, ML)
      object Regression extends ActionCategory(ActionCategory.Id.randomId, "Regression", Classification.priority.nextCore, ML)
      object Clustering extends ActionCategory(ActionCategory.Id.randomId, "Clustering", Regression.priority.nextCore, ML)
      object Evaluation extends ActionCategory(ActionCategory.Id.randomId, "Evaluation", Clustering.priority.nextCore, ML)
    }
    object Utils extends ActionCategory(ActionCategory.Id.randomId, "Utilities", ML.Evaluation.priority.nextCore)
  }

  abstract class ActionTypeTypeMock extends ActionType {
    def inputPorts = List.empty[TypeTag[_]]
    def outputPorts = List.empty[TypeTag[_]]

    override def inferKnowledgeUntyped(l: List[Knowledge[ActionObjectInfo]])(context: InferContext): (List[Knowledge[ActionObjectInfo]], InferenceWarnings) = ???

    def executeUntyped(l: List[ActionObjectInfo])(context: ExecutionContext): List[ActionObjectInfo] = ???

    val inArity: Int = 2
    val outArity: Int = 3

    override val parameterGroups = List.empty[ParameterGroup]

  }

  case class X() extends ActionObjectInfoMock
  case class Y() extends ActionObjectInfoMock

  val XTypeTag = typeTag[X]
  val YTypeTag = typeTag[Y]

  val idA = ActionTypeInfo.Id.randomId
  val idB = ActionTypeInfo.Id.randomId
  val idC = ActionTypeInfo.Id.randomId
  val idD = ActionTypeInfo.Id.randomId

  val nameA = "nameA"
  val nameB = "nameB"
  val nameC = "nameC"
  val nameD = "nameD"

  val versionA = "versionA"
  val versionB = "versionB"
  val versionC = "versionC"
  val versionD = "versionD"

  case class ActionTypeTypeA() extends ActionTypeTypeMock {
    val id = idA
    val name = nameA
  }

  case class ActionTypeTypeB() extends ActionTypeTypeMock {
    val id = idB
    val name = nameB
  }

  case class ActionTypeTypeC() extends ActionTypeTypeMock {
    val id = idC
    val name = nameC
  }

  case class ActionTypeTypeD() extends ActionTypeTypeMock {
    val id = idD
    val name = nameD
    override val inputPorts = List(XTypeTag, YTypeTag)
    override val outputPorts = List(XTypeTag)
  }
}

object ViewingTestResources extends MockitoSugar {

  import ActionCatalogTestResources._

  val categoryA = CategoryTree.ML.Regression
  val categoryB = CategoryTree.ML.Regression
  val categoryC = CategoryTree.ML.Classification
  val categoryD = CategoryTree.ML

  val catalog = new ActionCatalog()

  val priorityA = SortPriority(0)
  val priorityB = SortPriority(-1)
  val priorityC = SortPriority(2)
  val priorityD = SortPriority(3)

  catalog.registerAction(categoryA, () => new ActionTypeTypeA(), priorityA)
  catalog.registerAction(categoryB, () => new ActionTypeTypeB(), priorityB)
  catalog.registerAction(categoryC, () => new ActionTypeTypeC(), priorityC)
  catalog.registerAction(categoryD, () => new ActionTypeTypeD(), priorityD)

  val actionD = catalog.createAction(idD)

  val expectedA = ActionDescriptor(
    idA,
    nameA,
    descriptionA,
    categoryA,
    priorityA,
    hasDocumentation = false,
    ActionTypeTypeA().parametersToJson,
    Nil,
    List.empty,
    Nil,
    List.empty
  )

  val expectedB = shared.models.flow.catalogs.ActionDescriptor(
    idB,
    nameB,
    descriptionB,
    categoryB,
    priorityB,
    hasDocumentation = false,
    ActionTypeTypeB().parametersToJson,
    Nil,
    List.empty,
    Nil,
    List.empty
  )

  val expectedC = shared.models.flow.catalogs.ActionDescriptor(
    idC,
    nameC,
    descriptionC,
    categoryC,
    priorityC,
    hasDocumentation = false,
    ActionTypeTypeC().parametersToJson,
    Nil,
    List.empty,
    Nil,
    List.empty
  )

  val expectedD = ActionDescriptor(
    idD,
    nameD,
    descriptionD,
    categoryD,
    priorityD,
    hasDocumentation = false,
    ActionTypeTypeD().parametersToJson,
    List(XTypeTag.tpe, YTypeTag.tpe),
    actionD.inputPortsLayout,
    List(XTypeTag.tpe),
    actionD.outputPortsLayout
  )

}

class ActionTypeCatalogSuite extends AnyFunSuite with Matchers with MockitoSugar {

  test("It is possible to create instance of registered Action") {
    import ActionCatalogTestResources._
    val catalog  = new ActionCatalog()
    catalog.registerAction(CategoryTree.ML.Regression, () => new ActionTypeTypeA(), ViewingTestResources.priorityA)
    val instance = catalog.createAction(idA)
    assert(instance == ActionTypeTypeA())
  }

  test("Attempt of creating unregistered Action raises exception") {
    val nonExistingActionId = ActionTypeInfo.Id.randomId
    val exception = intercept[ActionNotFoundError] {
      ActionCatalog().createAction(nonExistingActionId)
    }
    exception.actionId shouldBe nonExistingActionId
  }

  test("It is possible to view list of registered Actions descriptors") {
    import ActionCatalogTestResources._
    import ViewingTestResources._

    val exceptUnknown = catalog.actions - new UnknownActionType().id
    exceptUnknown shouldBe Map(idA -> expectedA, idB -> expectedB, idC -> expectedC, idD -> expectedD)
  }

  test("SortPriority inSequence assigns values with step 100") {
    import com.harana.sdk.backend.models.flow.catalogs.actions.ActionCatalogTestResources.CategoryTree
    CategoryTree.ML.Classification.priority shouldBe SortPriority(300)
    CategoryTree.ML.Regression.priority shouldBe SortPriority(400)
  }

  test("It is possible to get tree of registered categories and Actions") {
    import ActionCatalogTestResources.CategoryTree._
    import ViewingTestResources._

    val root: ActionCategoryNode = catalog.categoryTree
    root.category shouldBe None
    root.actions shouldBe empty
    root.successors.keys should contain theSameElementsAs Seq(ML)

    val mlNode = root.successors(ML)
    mlNode.category shouldBe Some(ML)
    mlNode.actions shouldBe List(expectedD)
    mlNode.successors.keys.toList shouldBe List(ML.Classification, ML.Regression)

    val regressionNode = mlNode.successors(ML.Regression)
    regressionNode.category shouldBe Some(ML.Regression)
    regressionNode.actions shouldBe List(expectedB, expectedA)
    regressionNode.successors.keys shouldBe empty

    val classificationNode = mlNode.successors(ML.Classification)
    classificationNode.category shouldBe Some(ML.Classification)
    classificationNode.actions should contain theSameElementsAs Seq(expectedC)
    classificationNode.successors.keys shouldBe empty
  }
}
