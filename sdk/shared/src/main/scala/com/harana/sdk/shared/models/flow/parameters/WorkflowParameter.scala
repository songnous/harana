package com.harana.sdk.shared.models.flow.parameters

import com.harana.sdk.shared.models.flow.parameters.custom.InnerWorkflow


case class WorkflowParameter(name: String,
                             description: Option[String]) extends Parameter[InnerWorkflow] {

  val parameterType = ParameterType.Workflow

  override def validate(value: InnerWorkflow) = super.validate(value)

  override def replicate(name: String) = copy(name = name)

}