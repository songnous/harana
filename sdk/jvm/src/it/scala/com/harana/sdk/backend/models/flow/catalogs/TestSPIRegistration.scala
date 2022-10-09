package com.harana.sdk.backend.models.flow.catalogs

import com.harana.sdk.backend.models.flow.actiontypes.{ActionType, ActionTypeType0To1}
import scala.reflect.runtime.{universe => ru}
import com.harana.sdk.backend.models.flow.graph.DClassesForActions.A1
import com.harana.sdk.backend.models.flow.ExecutionContext
import com.harana.sdk.shared.models.designer.flow.ActionCategories
import com.harana.sdk.shared.models.designer.flow.utils.catalog.SortPriority
import com.harana.sdk.shared.models.flow.ActionTypeInfo
import com.harana.sdk.shared.models.flow.utils.Id

object SpiLoadedActionType {
  val spiLoadedActionUuid = "adf440aa-d3eb-4cb9-bf17-bb7fc1d34a0b"
  val spiLoadedActionId = ActionTypeInfo.Id.fromString(SpiLoadedActionType.spiLoadedActionUuid)
}

class SpiLoadedActionType extends ActionTypeType0To1[A1] {

  def execute()(context: ExecutionContext): A1 = ???

  @transient
  lazy val tTagTO_0: Tag[A1] = Tag[A1]

  val id: Id = SpiLoadedActionType.spiLoadedActionUuid
  val name = "SpiLoadedAction"
  override val parameterGroups = List.empty[ParameterGroup]
}

class TestSPIRegistration extends CatalogRegistrant {

  def register(registrar: CatalogRegistrar) =
    registrar.registerAction(ActionCategories.IO, () => new SpiLoadedActionType(), SortPriority(12345).inSequence(10).next)

}
