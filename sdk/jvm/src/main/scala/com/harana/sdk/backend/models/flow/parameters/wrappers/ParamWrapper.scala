package com.harana.sdk.backend.models.flow.parameters.wrappers

import com.harana.sdk.shared.models.flow.parameters.Parameter
import org.apache.spark.ml.param.Param

class ParamWrapper[T](parentId: String, val param: Parameter[T])
    extends Param[T](parentId, param.name, param.description.getOrElse(""), (_: T) => true)

object ParamWrapper {
  def isValid[T](param: Parameter[T], value: T): Boolean = param.validate(value).isEmpty
}
