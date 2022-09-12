package com.harana.sdk.backend.models.flow.actionobjects.spark.wrappers.estimators.stringindexingwrapper

import com.harana.sdk.backend.models.flow.UnitSpec
import com.harana.sdk.backend.models.flow.actionobjects.spark.wrappers.estimators.{GBTClassifier, VanillaGBTClassifier}

class StringIndexingWrapperSpec extends UnitSpec {

  "String indexing wrapper" should {
    "inherit default values from wrapped estimator" in {
      val someWrappedEstimator = new VanillaGBTClassifier()
      val someWrapper = new GBTClassifier

      for (someParameter <- someWrapper.parameters)
        someWrapper.getDefault(someParameter) shouldEqual someWrappedEstimator.getDefault(someParameter)
    }
  }
}
