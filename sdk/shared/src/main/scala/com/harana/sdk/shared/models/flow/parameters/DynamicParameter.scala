package com.harana.sdk.shared.models.flow.parameters

import io.circe.Json

class DynamicParameter(val name: String,
                       val description: Option[String],
                       val inputPort: Int) extends Parameter[Json] {

  val parameterType: ParameterType = ParameterType.Dynamic

  override def replicate(name: String): DynamicParameter = new DynamicParameter(name, description, inputPort)

}