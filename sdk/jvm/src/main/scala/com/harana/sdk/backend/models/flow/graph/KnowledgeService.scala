package com.harana.sdk.backend.models.flow.graph

import com.harana.sdk.backend.models.designer.flow.Catalog.ActionObjectCatalog
import com.harana.sdk.backend.models.designer.flow.Knowledge
import com.harana.sdk.backend.models.flow.Catalog.ActionObjectCatalog
import com.harana.sdk.shared.models.flow.{ActionInfo, ActionObjectInfo}

import scala.reflect.runtime.{universe => ru}

object KnowledgeService {

  // Knowledge vector for output ports if no additional information is provided
  def defaultOutputKnowledge(catalog: ActionObjectCatalog, action: ActionInfo): Vector[Knowledge[ActionObjectInfo]] =
    for (outPortType <- action.outPortTypes) yield defaultKnowledge(catalog, outPortType)

  // Knowledge vector for input ports if no additional information is provided
  def defaultInputKnowledge(catalog: ActionObjectCatalog, action: ActionInfo): Vector[Knowledge[ActionObjectInfo]] =
    for (inPortType <- action.inPortTypes) yield defaultKnowledge(catalog, inPortType)

  // Knowledge for port if no additional information is provided
  def defaultKnowledge(catalog: ActionObjectCatalog, portType: ru.TypeTag[_]): Knowledge[ActionObjectInfo] = {
    val castedType = portType.asInstanceOf[ru.TypeTag[ActionObjectInfo]]
    Knowledge(catalog.concreteSubclassesInstances(castedType))
  }
}