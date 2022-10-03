package com.harana.sdk.backend.models.flow.catalogs.actionobjects

import com.harana.sdk.backend.models.flow.UnitSpec
import com.harana.sdk.shared.models.flow.actionobjects.ActionObjectInfo

class ActionObjectRegistrationSpec extends UnitSpec {

  "ActionObjectCatalog" should {
    "successfully register and create all ActionObjects" in {
      val catalog = CatalogRecorder.resourcesCatalogRecorder.catalogs.actionObjects
      catalog.concreteSubclassesInstances[ActionObjectInfo]
    }
  }
}
