package com.harana.sdk.backend.models.flow.catalogs

import scala.reflect.runtime.{universe => ru}
import com.harana.sdk.backend.models.flow.graph.DClassesForActions.A1
import com.harana.sdk.backend.models.flow.{Action, Action0To1, ExecutionContext}
import com.harana.sdk.shared.models.designer.flow.ActionCategories
import com.harana.sdk.shared.models.designer.flow.utils.catalog.SortPriority
import com.harana.sdk.shared.models.flow.ActionInfo
import com.harana.sdk.shared.models.flow.utils.Id

object SpiLoadedAction {
  val spiLoadedActionUuid = "adf440aa-d3eb-4cb9-bf17-bb7fc1d34a0b"
  val spiLoadedActionId = ActionInfo.Id.fromString(SpiLoadedAction.spiLoadedActionUuid)
}

class SpiLoadedAction extends Action0To1[A1] {

  def execute()(context: ExecutionContext): A1 = ???

  @transient
  lazy val tTagTO_0: ru.TypeTag[A1] = ru.typeTag[A1]

  val id: Id = SpiLoadedAction.spiLoadedActionUuid
  val name = "SpiLoadedAction"
  val parameters = Left(Array.empty[Parameter[_]])
}

class TestSPIRegistration extends CatalogRegistrant {

  def register(registrar: CatalogRegistrar) =
    registrar.registerAction(ActionCategories.IO, () => new SpiLoadedAction(), SortPriority(12345).inSequence(10).next)

}
