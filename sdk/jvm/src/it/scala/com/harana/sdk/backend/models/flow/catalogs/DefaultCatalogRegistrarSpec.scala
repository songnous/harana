package com.harana.sdk.backend.models.flow.catalogs

import com.harana.sdk.backend.models.flow.catalogs.spi.CatalogRegistrant
import com.harana.sdk.backend.models.flow.catalogs.spi.CatalogRegistrar.DefaultCatalogRegistrar
import com.harana.sdk.backend.models.flow.UnitSpec
import com.harana.sdk.shared.models.flow.ActionInfo
import org.scalatest.BeforeAndAfter

class DefaultCatalogRegistrarSpec extends UnitSpec with BeforeAndAfter {

  val defaultCatalogRegistrar = new DefaultCatalogRegistrar()
  val actionCatalog = defaultCatalogRegistrar.catalog.actions

  CatalogRegistrant.load(defaultCatalogRegistrar, null)

  "Default Catalog Registrar" should {

    "contain action loaded using test registrator" in {
      actionCatalog.actions.keys should contain(SpiLoadedAction.spiLoadedActionId)
    }

    "create action loaded using test registrator" in {
      val action = actionCatalog.createAction(SpiLoadedAction.spiLoadedActionUuid)
      action.id shouldBe SpiLoadedAction.spiLoadedActionId
    }
  }
}