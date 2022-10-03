package com.harana.sdk.backend.models.flow.catalogs.actions

import com.harana.sdk.backend.models.flow.{CatalogRecorder, UnitSpec}

class ActionTypeRegistrationSpec extends UnitSpec {

  "ActionsCatalog" should {
    val catalogs   = CatalogRecorder.resourcesCatalogRecorder.catalogs
    val actions = catalogs.actions

    "successfully register and create all Actions" in {
      actions.actions.keys.foreach(id => actions.createAction(id))
    }

    "report assigned categories" in {
      val delta = catalogs.categories.diff(actions.categories)
      delta shouldBe empty
    }

  }
}
