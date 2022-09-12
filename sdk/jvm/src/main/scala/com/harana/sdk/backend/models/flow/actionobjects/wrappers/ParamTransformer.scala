package com.harana.sdk.backend.models.flow.actionobjects.wrappers

import com.harana.sdk.backend.models.flow.parameters.wrappers.ParamWrapper
import com.harana.sdk.shared.models.flow.parameters.{Parameter, ParameterPair}
import org.apache.spark.ml.param.ParamMap

object ParamTransformer {

  /** Transforms spark ParamMap to a seq of flow ParamPair. Transformation is needed when operating on flow
    * Estimators, Transformers, Evaluators wrapped to work as Spark entities.
    */
  def transform(sparkParamMap: ParamMap): Seq[ParameterPair[_]] = {
    sparkParamMap.toSeq.map { sparkParamPair =>
      val param: Parameter[Any] = sparkParamPair.param.asInstanceOf[ParamWrapper[Any]].param
      ParameterPair(param, sparkParamPair.value)
    }
  }
}
