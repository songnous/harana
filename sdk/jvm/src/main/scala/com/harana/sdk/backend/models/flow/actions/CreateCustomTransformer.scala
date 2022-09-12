package com.harana.sdk.backend.models.flow.actions

import com.harana.sdk.backend.models.designer.flow._
import com.harana.sdk.backend.models.flow.ExecutionContext
import com.harana.sdk.backend.models.flow.actionobjects.CustomTransformer
import com.harana.sdk.backend.models.flow.utils.CustomTransformerFactory
import com.harana.sdk.shared.models.flow.actions.CreateCustomTransformerInfo

import scala.reflect.runtime.universe.TypeTag

class CreateCustomTransformer() extends TransformerAsFactory[CustomTransformer] with CreateCustomTransformerInfo {

  override def execute()(context: ExecutionContext) = CustomTransformerFactory.createCustomTransformer(getInnerWorkflow)

  lazy val tTagTO_0: TypeTag[CustomTransformer] = typeTag

}