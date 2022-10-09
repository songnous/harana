package com.harana.sdk.backend.models.flow.utils

import com.harana.sdk.backend.models.flow.actionobjects.CustomTransformer
import com.harana.sdk.shared.models.flow.actionobjects.ParameterWithValues
import com.harana.sdk.shared.models.flow.parameters.custom.{InnerWorkflow, PublicParameter}

object CustomTransformerFactory {

  def createCustomTransformer(innerWorkflow: InnerWorkflow) = {
    val selectedParameters: Seq[ParameterWithValues[_]] =
      innerWorkflow.publicParameters.flatMap { case PublicParameter(nodeId, parameterName, publicName) =>
        innerWorkflow.graph.nodes
          .find(_.id == nodeId)
          .flatMap(node =>
            node.value.typeInfo.allParameters
              .find(_.name == parameterName)
              .map(p => {
                ParameterWithValues(
                  param = p.replicate(publicName),
                  defaultValue = node.value.typeInfo.getDefault(p),
                  setValue = node.value.typeInfo.get(p)
                )
              })
          )
      }
    new CustomTransformer(innerWorkflow, selectedParameters)
  }
}
