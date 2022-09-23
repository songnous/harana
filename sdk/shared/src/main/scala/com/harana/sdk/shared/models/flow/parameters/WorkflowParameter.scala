package com.harana.sdk.shared.models.flow.parameters

import com.harana.sdk.shared.models.flow.parameters.custom.InnerWorkflow
import io.circe.generic.JsonCodec

@JsonCodec
case class WorkflowParameter(name: String,
                             required: Boolean = false,
                             default: Option[InnerWorkflow] = None) extends Parameter[InnerWorkflow] {

  val parameterType = ParameterType.Workflow

  override def validate(value: InnerWorkflow) = super.validate(value)

  override def replicate(name: String) = copy(name = name)

}