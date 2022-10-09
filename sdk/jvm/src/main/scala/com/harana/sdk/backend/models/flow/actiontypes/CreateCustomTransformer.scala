package com.harana.sdk.backend.models.flow.actiontypes

import com.harana.sdk.backend.models.flow.ExecutionContext
import com.harana.sdk.backend.models.flow.actionobjects.CustomTransformer
import com.harana.sdk.backend.models.flow.utils.CustomTransformerFactory
import com.harana.sdk.shared.models.flow.actiontypes.CreateCustomTransformerInfo

import izumi.reflect.Tag

class CreateCustomTransformer() extends TransformerAsFactory[CustomTransformer] with CreateCustomTransformerInfo {

  override def execute()(context: ExecutionContext) = CustomTransformerFactory.createCustomTransformer(getInnerWorkflow)

  lazy val tTagTO_0: Tag[CustomTransformer] = typeTag

}