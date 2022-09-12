package com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.parameters

import com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.parameters.common._
import com.harana.sdk.shared.models.flow.parameters.Parameters

trait LinearRegressionParameters
  extends Parameters
  with PredictorParameters
  with HasLabelColumnParameter
  with HasRegularizationParameter
  with HasElasticNetParameter
  with HasMaxIterationsParameter
  with HasToleranceParameter
  with HasFitInterceptParameter
  with HasStandardizationParameter
  with HasOptionalWeightColumnParameter
  with HasSolverParameter
