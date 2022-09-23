package com.harana.sdk.shared.models.flow.parameters

import io.circe.Json

class DynamicParameter(val name: String,
                       val required: Boolean = false,
                       val inputPort: Int) extends Parameter[Json] {

  val parameterType: ParameterType = ParameterType.Dynamic

  override def replicate(name: String): DynamicParameter = new DynamicParameter(name, required, inputPort)

}