package com.harana.sdk.backend.models.flow.catalogs.actions

import com.harana.sdk.backend.models.designer.flow.Catalog.ActionCatalog
import com.harana.sdk.backend.models.designer.flow._
import com.harana.sdk.backend.models.designer.flow.actionobjects.ActionObjectInfoMock
import com.harana.sdk.backend.models.designer.flow.actions.UnknownAction
import com.harana.sdk.backend.models.designer.flow.inference.{InferContext, InferenceWarnings}
import com.harana.sdk.backend.models.flow.actionobjects.ActionObjectInfoMock
import com.harana.sdk.backend.models.flow.{ExecutionContext, Knowledge}
import com.harana.sdk.backend.models.flow.inference.{InferContext, InferenceWarnings}
import com.harana.sdk.shared
import com.harana.sdk.shared.models.designer.flow
import com.harana.sdk.shared.models.flow.{ActionInfo, ActionObjectInfo}
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

  abstract class ActionMock extends Action {
    def inPortTypes = Vector.empty[TypeTag[_]]
    def outPortTypes = Vector.empty[TypeTag[_]]

    override def inferKnowledgeUntyped(l: Vector[Knowledge[ActionObjectInfo]])(context: InferContext): (Vector[Knowledge[ActionObjectInfo]], InferenceWarnings) = ???

    def executeUntyped(l: Vector[ActionObjectInfo])(context: ExecutionContext): Vector[ActionObjectInfo] = ???

    val inArity: Int = 2
    val outArity: Int = 3

    val parameters = Array.empty[Parameter[_]]

  }

  case class X() extends ActionObjectInfoMock
  case class Y() extends ActionObjectInfoMock

  val XTypeTag = typeTag[X]
  val YTypeTag = typeTag[Y]

  val idA = ActionInfo.Id.randomId
  val idB = ActionInfo.Id.randomId
  val idC = ActionInfo.Id.randomId
  val idD = ActionInfo.Id.randomId

  val nameA = "nameA"
  val nameB = "nameB"
  val nameC = "nameC"
  val nameD = "nameD"

  val descriptionA = "descriptionA"
  val descriptionB = "descriptionB"
  val descriptionC = "descriptionC"
  val descriptionD = "descriptionD"

  val versionA = "versionA"
  val versionB = "versionB"
  val versionC = "versionC"
  val versionD = "versionD"

  case class ActionA() extends ActionMock {
    val id = idA
    val name = nameA
    val description = descriptionA
  }

  case class ActionB() extends ActionMock {
    val id = idB
    val name = nameB
    val description = descriptionB
  }

  case class ActionC() extends ActionMock {
    val id = idC
    val name = nameC
    val description = descriptionC
  }

  case class ActionD() extends ActionMock {
    val id = idD
    val name = nameD
    val description = descriptionD
    override val inPortTypes = Vector(XTypeTag, YTypeTag)
    override val outPortTypes = Vector(XTypeTag)
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

  catalog.registerAction(categoryA, () => new ActionA(), priorityA)
  catalog.registerAction(categoryB, () => new ActionB(), priorityB)
  catalog.registerAction(categoryC, () => new ActionC(), priorityC)
  catalog.registerAction(categoryD, () => new ActionD(), priorityD)

  val actionD = catalog.createAction(idD)

  val expectedA = ActionDescriptor(
    idA,
    nameA,
    descriptionA,
    categoryA,
    priorityA,
    hasDocumentation = false,
    ActionA().parametersToJson,
    Nil,
    Vector.empty,
    Nil,
    Vector.empty
  )

  val expectedB = shared.models.flow.catalogs.ActionDescriptor(
    idB,
    nameB,
    descriptionB,
    categoryB,
    priorityB,
    hasDocumentation = false,
    ActionB().parametersToJson,
    Nil,
    Vector.empty,
    Nil,
    Vector.empty
  )

  val expectedC = shared.models.flow.catalogs.ActionDescriptor(
    idC,
    nameC,
    descriptionC,
    categoryC,
    priorityC,
    hasDocumentation = false,
    ActionC().parametersToJson,
    Nil,
    Vector.empty,
    Nil,
    Vector.empty
  )

  val expectedD = ActionDescriptor(
    idD,
    nameD,
    descriptionD,
    categoryD,
    priorityD,
    hasDocumentation = false,
    ActionD().parametersToJson,
    List(XTypeTag.tpe, YTypeTag.tpe),
    actionD.inPortsLayout,
    List(XTypeTag.tpe),
    actionD.outPortsLayout
  )

}

class ActionCatalogSuite extends AnyFunSuite with Matchers with MockitoSugar {

  test("It is possible to create instance of registered Action") {
    import ActionCatalogTestResources._
    val catalog  = new ActionCatalog()
    catalog.registerAction(CategoryTree.ML.Regression, () => new ActionA(), ViewingTestResources.priorityA)
    val instance = catalog.createAction(idA)
    assert(instance == ActionA())
  }

  test("Attempt of creating unregistered Action raises exception") {
    val nonExistingActionId = ActionInfo.Id.randomId
    val exception = intercept[ActionNotFoundError] {
      ActionCatalog().createAction(nonExistingActionId)
    }
    exception.actionId shouldBe nonExistingActionId
  }

  test("It is possible to view list of registered Actions descriptors") {
    import ActionCatalogTestResources._
    import ViewingTestResources._

    val exceptUnknown = catalog.actions - new UnknownAction().id
    exceptUnknown shouldBe Map(idA -> expectedA, idB -> expectedB, idC -> expectedC, idD -> expectedD)
  }

  test("SortPriority inSequence assigns values with step 100") {
    import com.harana.sdk.backend.models.designer.flow.catalogs.actions.ActionCatalogTestResources.CategoryTree
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
