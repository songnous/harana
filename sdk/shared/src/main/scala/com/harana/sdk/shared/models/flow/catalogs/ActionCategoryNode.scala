package com.harana.sdk.shared.models.flow.catalogs

import com.harana.sdk.shared.models.flow.ActionTypeInfo
import com.harana.sdk.shared.models.flow.utils.SortPriority
import io.circe.generic.JsonCodec

import scala.collection.immutable.SortedMap

@JsonCodec
case class ActionCategoryNode(category: Option[ActionCategory] = None,
                              successors: SortedMap[ActionCategory, ActionCategoryNode] = ActionCategoryNode.emptyInnerNodes,
                              actions: List[ActionDescriptor] = List.empty) {

  private def addActionAtPath(action: ActionDescriptor, path: List[ActionCategory]): ActionCategoryNode = {
    path match {
      case Nil => copy(actions = (actions :+ action).sortWith(_.priority < _.priority))
      case category :: tail =>
        val successor = successors.getOrElse(category, ActionCategoryNode(Some(category)))
        val updatedSuccessor = successor.addActionAtPath(action, tail)
        copy(successors = successors + (category -> updatedSuccessor))
    }
  }

  def addAction(action: ActionDescriptor, category: ActionCategory) =
    addActionAtPath(action, category.pathFromRoot)

  def getActions: List[(ActionCategory, ActionTypeInfo.Id, SortPriority)] = {
    val thisActions = actions.map(action => (action.category, action.id, action.priority))
    val successorActions = successors.collect { case successor => successor._2.getActions }.flatten
    thisActions ++ successorActions
  }

  def getCategories: Seq[ActionCategory] = {
    val successorActions = successors.collect { case successor => successor._2.getCategories }.flatten
    successorActions.toList ++ category
  }
}

object ActionCategoryNode {
  def emptyInnerNodes = SortedMap[ActionCategory, ActionCategoryNode]()
}
