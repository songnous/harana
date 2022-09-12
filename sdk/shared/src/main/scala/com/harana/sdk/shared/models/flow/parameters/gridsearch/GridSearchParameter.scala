package com.harana.sdk.shared.models.flow.parameters.gridsearch

import com.harana.sdk.shared.models.flow.parameters.{DynamicParameter, ParameterType}
import com.harana.sdk.shared.models.flow.parameters.ParameterType.GridSearch

class GridSearchParameter(override val name: String, override val description: Option[String], override val inputPort: Int) extends DynamicParameter(name, description, inputPort) {

  override val parameterType = GridSearch

  override def replicate(name: String) = new GridSearchParameter(name, description, inputPort)

}
