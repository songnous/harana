package com.harana.sdk.shared.models.flow.parameters.spark

case class ParamPair[T] (param: Param[T], value: T)