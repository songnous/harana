package com.harana.sdk.shared.models.flow.actionobjects

import com.harana.sdk.shared.models.flow.parameters.{Parameter, ParameterPair}

trait SparkModelWrapperInfo extends TransformerInfo {

  override def setDefault[T](param: Parameter[T], value: T): this.type = this
  override def setDefault(paramPairs: ParameterPair[_]*): this.type = this

}