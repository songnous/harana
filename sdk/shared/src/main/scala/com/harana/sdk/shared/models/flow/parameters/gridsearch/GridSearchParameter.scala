package com.harana.sdk.shared.models.flow.parameters.gridsearch

import com.harana.sdk.shared.models.flow.parameters.{DynamicParameter, ParameterType}
import com.harana.sdk.shared.models.flow.parameters.ParameterType.GridSearch
import io.circe.Json

class GridSearchParameter(override val name: String,
                          override val required: Boolean = false,
                          override val default: Option[Json] = None,
                          override val inputPort: Int) extends DynamicParameter(name, required, default, inputPort) {

  override val parameterType = GridSearch

  override def replicate(name: String) = new GridSearchParameter(name, required, default, inputPort)

}
