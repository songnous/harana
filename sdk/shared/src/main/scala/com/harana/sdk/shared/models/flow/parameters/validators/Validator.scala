package com.harana.sdk.shared.models.flow.parameters.validators

import com.harana.sdk.shared.models.flow.exceptions.FlowError

@SerialVersionUID(1)
trait Validator[ParameterType] extends Serializable {

  val validatorType: ValidatorType

  def validate(name: String, parameter: ParameterType): Vector[FlowError]

  def toHumanReadable(parameterName: String) = ""

}
