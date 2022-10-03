package com.harana.sdk.backend.models.flow.graph

import com.harana.sdk.backend.models.flow.Catalog.ActionObjectCatalog
import com.harana.sdk.backend.models.flow.Knowledge
import com.harana.sdk.backend.models.flow.Catalog.ActionObjectCatalog
import com.harana.sdk.shared.models.flow.ActionTypeInfo
import com.harana.sdk.shared.models.flow.actionobjects.ActionObjectInfo

import scala.reflect.runtime.{universe => ru}

object KnowledgeService {

  // Knowledge vector for output ports if no additional information is provided
  def defaultOutputKnowledge(catalog: ActionObjectCatalog, action: ActionTypeInfo): List[Knowledge[ActionObjectInfo]] =
    for (outPortType <- action.outputPorts) yield defaultKnowledge(catalog, outPortType)

  // Knowledge vector for input ports if no additional information is provided
  def defaultInputKnowledge(catalog: ActionObjectCatalog, action: ActionTypeInfo): List[Knowledge[ActionObjectInfo]] =
    for (inPortType <- action.inputPorts) yield defaultKnowledge(catalog, inPortType)

  // Knowledge for port if no additional information is provided
  def defaultKnowledge(catalog: ActionObjectCatalog, portType: ru.TypeTag[_]): Knowledge[ActionObjectInfo] = {
    val castedType = portType.asInstanceOf[ru.TypeTag[ActionObjectInfo]]
    Knowledge(catalog.concreteSubclassesInstances(castedType))
  }
}