package com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.estimators

import com.harana.sdk.shared.models.flow.actionobjects.EstimatorInfo
import com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.parameters.common.{HasFeatureIndexParameter, HasLabelColumnParameter, HasOptionalWeightColumnParameter, PredictorParameters}
import com.harana.sdk.shared.models.flow.parameters.{BooleanParameter, ParameterGroup}

trait IsotonicRegressionInfo
    extends EstimatorInfo
    with PredictorParameters
    with HasFeatureIndexParameter
    with HasLabelColumnParameter
    with HasOptionalWeightColumnParameter {

  val id = "007FBD57-E166-4892-95AA-F8D9DC526BFA"

  val isotonicParameter = BooleanParameter("isotonic", default = Some(true))

  override val parameterGroups = List(ParameterGroup("",
    isotonicParameter,
    optionalWeightColumnParameter,
    featureIndexParameter,
    labelColumnParameter,
    featuresColumnParameter,
    predictionColumnParameter
  ))
}

object IsotonicRegressionInfo extends IsotonicRegressionInfo