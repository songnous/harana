package com.harana.sdk.backend.models.flow.actionobjects

import com.harana.sdk.backend.models.designer.flow.UnitSpec
import com.harana.sdk.backend.models.designer.flow.actionobjects.spark.wrappers.models.MultiColumnStringIndexerModel
import com.harana.sdk.shared.models.flow.parameters.ParameterMap

class MultiColumnModelSpec extends UnitSpec {

  "MultiColumnModel" should {
    "not fail during replicate" in {
      val model = new MultiColumnStringIndexerModel()
      model.setModels(Seq())
      model.replicate(ParameterMap.empty)
    }
  }
}
