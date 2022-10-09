package com.harana.sdk.backend.models.flow.actiontypes

import com.harana.sdk.backend.models.flow.actiontypes.ActionTypeConversions._
import com.harana.sdk.backend.models.flow.inference.{InferContext, InferenceWarnings}
import com.harana.sdk.backend.models.flow.{ExecutionContext, Knowledge}
import com.harana.sdk.shared.models.flow.ActionTypeConversions._
import com.harana.sdk.shared.models.flow.actionobjects.ActionObjectInfo
import izumi.reflect.Tag

import scala.reflect.runtime.{universe => ru}

object ActionTypeConversions {
  implicit def dKnowledgeSingletonToList[T1 <: ActionObjectInfo](t: (Knowledge[T1], InferenceWarnings)) =
    (List(t._1), t._2)

  implicit def dKnowledgeTuple2ToList[T1 <: ActionObjectInfo, T2 <: ActionObjectInfo](t: ((Knowledge[T1], Knowledge[T2]), InferenceWarnings)) = {
    val (k, w) = t
    (List(k._1, k._2), w)
  }

  implicit def dKnowledgeTuple3ToList[T1 <: ActionObjectInfo, T2 <: ActionObjectInfo, T3 <: ActionObjectInfo](t: ((Knowledge[T1], Knowledge[T2], Knowledge[T3]), InferenceWarnings)) = {
    val (k, w) = t
    (List(k._1, k._2, k._3), w)
  }
}

trait ActionTypeType0To1[TO_0 <: ActionObjectInfo] extends ActionType {
  final def executeUntyped(arguments: List[ActionObjectInfo])(context: ExecutionContext) =
    execute()(context)

  def execute()(context: ExecutionContext): TO_0

  final def inferKnowledgeUntyped(knowledge: List[Knowledge[ActionObjectInfo]])(context: InferContext) =
    inferKnowledge()(context)

  def inferKnowledge()(context: InferContext): (Knowledge[TO_0], InferenceWarnings) =
    (Knowledge(context.actionObjectCatalog.concreteSubclassesInstances[TO_0](tTagTO_0)), InferenceWarnings.empty)

  val tTagTO_0: Tag[TO_0]
}

trait ActionTypeType0To2[TO_0 <: ActionObjectInfo, TO_1 <: ActionObjectInfo] extends ActionType {
  final def executeUntyped(arguments: List[ActionObjectInfo])(context: ExecutionContext): List[ActionObjectInfo] =
    (execute()(context))

  def execute()(context: ExecutionContext): (TO_0, TO_1)

  final def inferKnowledgeUntyped(knowledge: List[Knowledge[ActionObjectInfo]])(context: InferContext): (List[Knowledge[ActionObjectInfo]], InferenceWarnings) =
    inferKnowledge()(context)

  def inferKnowledge()(context: InferContext): ((Knowledge[TO_0], Knowledge[TO_1]), InferenceWarnings) = {
    (
      (
        Knowledge(context.actionObjectCatalog.concreteSubclassesInstances[TO_0](tTagTO_0)),
        Knowledge(context.actionObjectCatalog.concreteSubclassesInstances[TO_1](tTagTO_1))
      ),
      InferenceWarnings.empty
    )
  }

  val tTagTO_0: Tag[TO_0]
  val tTagTO_1: Tag[TO_1]
}

trait ActionTypeType0To3[TO_0 <: ActionObjectInfo, TO_1 <: ActionObjectInfo, TO_2 <: ActionObjectInfo] extends ActionType {
  def execute()(context: ExecutionContext): (TO_0, TO_1)

  final def executeUntyped(arguments: List[ActionObjectInfo])(context: ExecutionContext): List[ActionObjectInfo] =
    execute()(context)

  final def inferKnowledgeUntyped(knowledge: List[Knowledge[ActionObjectInfo]])(context: InferContext): (List[Knowledge[ActionObjectInfo]], InferenceWarnings) =
    inferKnowledge()(context)

  def inferKnowledge()(
    context: InferContext
  ): ((Knowledge[TO_0], Knowledge[TO_1], Knowledge[TO_2]), InferenceWarnings) = {
    (
      (
        Knowledge(context.actionObjectCatalog.concreteSubclassesInstances[TO_0](tTagTO_0)),
        Knowledge(context.actionObjectCatalog.concreteSubclassesInstances[TO_1](tTagTO_1)),
        Knowledge(context.actionObjectCatalog.concreteSubclassesInstances[TO_2](tTagTO_2))
      ),
      InferenceWarnings.empty
    )
  }

  val tTagTO_0: Tag[TO_0]
  val tTagTO_1: Tag[TO_1]
  val tTagTO_2: Tag[TO_2]
}

trait ActionTypeType1To0[TI_0 <: ActionObjectInfo] extends ActionType {

  final def executeUntyped(arguments: List[ActionObjectInfo])(context: ExecutionContext): List[ActionObjectInfo] = {
    execute(arguments(0).asInstanceOf[TI_0])(context)
    List.empty
  }

  def execute(t0: TI_0)(context: ExecutionContext): Unit

  final def inferKnowledgeUntyped(knowledge: List[Knowledge[ActionObjectInfo]])(context: InferContext) = {
    inferKnowledge(knowledge.head.asInstanceOf[Knowledge[TI_0]])(context)
    (List.empty[Knowledge[ActionObjectInfo]], InferenceWarnings.empty)
  }

  def inferKnowledge(k0: Knowledge[TI_0])(context: InferContext): (Unit, InferenceWarnings) = ((), InferenceWarnings.empty)
}

trait ActionTypeType1To1[TI_0 <: ActionObjectInfo, TO_0 <: ActionObjectInfo] extends ActionType {

  final def executeUntyped(arguments: List[ActionObjectInfo])(context: ExecutionContext): List[ActionObjectInfo] =
    execute(arguments.head.asInstanceOf[TI_0])(context)

  def execute(t0: TI_0)(context: ExecutionContext): TO_0

  final def inferKnowledgeUntyped(knowledge: List[Knowledge[ActionObjectInfo]])(context: InferContext): (List[Knowledge[ActionObjectInfo]], InferenceWarnings) =
    inferKnowledge(knowledge(0).asInstanceOf[Knowledge[TI_0]])(context)

  def inferKnowledge(k0: Knowledge[TI_0])(context: InferContext): (Knowledge[TO_0], InferenceWarnings) =
    (Knowledge(context.actionObjectCatalog.concreteSubclassesInstances[TO_0](tTagTO_0)), InferenceWarnings.empty)

  val tTagTO_0: izumi.reflect.Tag[TO_0]
}

trait ActionTypeType1To2[
  TI_0 <: ActionObjectInfo,
  TO_0 <: ActionObjectInfo,
  TO_1 <: ActionObjectInfo] extends ActionType {

  final def executeUntyped(arguments: List[ActionObjectInfo])(context: ExecutionContext): List[ActionObjectInfo] =
    execute(arguments(0).asInstanceOf[TI_0])(context)

  def execute(t0: TI_0)(context: ExecutionContext): (TO_0, TO_1)

  final def inferKnowledgeUntyped(knowledge: List[Knowledge[ActionObjectInfo]])(context: InferContext): (List[Knowledge[ActionObjectInfo]], InferenceWarnings) =
    inferKnowledge(knowledge(0).asInstanceOf[Knowledge[TI_0]])(context)

  def inferKnowledge(k0: Knowledge[TI_0])(context: InferContext): ((Knowledge[TO_0], Knowledge[TO_1]), InferenceWarnings) = {
    (
      (
        Knowledge(context.actionObjectCatalog.concreteSubclassesInstances[TO_0](tTagTO_0)),
        Knowledge(context.actionObjectCatalog.concreteSubclassesInstances[TO_1](tTagTO_1))
      ),
      InferenceWarnings.empty
    )
  }

  val tTagTO_0: Tag[TO_0]
  val tTagTO_1: Tag[TO_1]
}

trait ActionTypeType1To3[
  TI_0 <: ActionObjectInfo,
  TO_0 <: ActionObjectInfo,
  TO_1 <: ActionObjectInfo,
  TO_2 <: ActionObjectInfo] extends ActionType {

  final def executeUntyped(arguments: List[ActionObjectInfo])(context: ExecutionContext): List[ActionObjectInfo] =
    execute(arguments(0).asInstanceOf[TI_0])(context)

  def execute(t0: TI_0)(context: ExecutionContext): (TO_0, TO_1, TO_2)

  final def inferKnowledgeUntyped(knowledge: List[Knowledge[ActionObjectInfo]])(context: InferContext): (List[Knowledge[ActionObjectInfo]], InferenceWarnings) =
    inferKnowledge(knowledge(0).asInstanceOf[Knowledge[TI_0]])(context)

  def inferKnowledge(k0: Knowledge[TI_0])(context: InferContext): ((Knowledge[TO_0], Knowledge[TO_1], Knowledge[TO_2]), InferenceWarnings) = {
    (
      (
        Knowledge(context.actionObjectCatalog.concreteSubclassesInstances[TO_0](tTagTO_0)),
        Knowledge(context.actionObjectCatalog.concreteSubclassesInstances[TO_1](tTagTO_1)),
        Knowledge(context.actionObjectCatalog.concreteSubclassesInstances[TO_2](tTagTO_2))
      ),
      InferenceWarnings.empty
    )
  }

  val tTagTO_0: Tag[TO_0]
  val tTagTO_1: Tag[TO_1]
  val tTagTO_2: Tag[TO_2]
}

trait ActionTypeType2To0[TI_0 <: ActionObjectInfo, TI_1 <: ActionObjectInfo] extends ActionType {

  final def executeUntyped(arguments: List[ActionObjectInfo])(context: ExecutionContext): List[ActionObjectInfo] = {
    execute(arguments(0).asInstanceOf[TI_0], arguments(1).asInstanceOf[TI_1])(context)
    List.empty
  }

  def execute(t0: TI_0, t1: TI_1)(context: ExecutionContext): Unit

  final def inferKnowledgeUntyped(knowledge: List[Knowledge[ActionObjectInfo]])(context: InferContext): (List[Knowledge[ActionObjectInfo]], InferenceWarnings) = {
    inferKnowledge(knowledge(0).asInstanceOf[Knowledge[TI_0]], knowledge(1).asInstanceOf[Knowledge[TI_1]])(context)
    (List.empty, InferenceWarnings.empty)
  }

  def inferKnowledge(k0: Knowledge[TI_0], k1: Knowledge[TI_1])(context: InferContext): (Unit, InferenceWarnings) =
    ((), InferenceWarnings.empty)
}

trait ActionTypeType2To1[
  TI_0 <: ActionObjectInfo,
  TI_1 <: ActionObjectInfo,
  TO_0 <: ActionObjectInfo] extends ActionType {

  final def executeUntyped(arguments: List[ActionObjectInfo])(context: ExecutionContext): List[ActionObjectInfo] =
    execute(arguments(0).asInstanceOf[TI_0], arguments(1).asInstanceOf[TI_1])(context)

  def execute(t0: TI_0, t1: TI_1)(context: ExecutionContext): TO_0

  final def inferKnowledgeUntyped(knowledge: List[Knowledge[ActionObjectInfo]])(context: InferContext): (List[Knowledge[ActionObjectInfo]], InferenceWarnings) =
    inferKnowledge(knowledge(0).asInstanceOf[Knowledge[TI_0]], knowledge(1).asInstanceOf[Knowledge[TI_1]])(context)

  def inferKnowledge(k0: Knowledge[TI_0], k1: Knowledge[TI_1])(context: InferContext): (Knowledge[TO_0], InferenceWarnings) =
    (Knowledge(context.actionObjectCatalog.concreteSubclassesInstances[TO_0](tTagTO_0)), InferenceWarnings.empty)

  val tTagTO_0: Tag[TO_0]
}

trait ActionTypeType2To2[
  TI_0 <: ActionObjectInfo,
  TI_1 <: ActionObjectInfo,
  TO_0 <: ActionObjectInfo,
  TO_1 <: ActionObjectInfo] extends ActionType {

  final def executeUntyped(arguments: List[ActionObjectInfo])(context: ExecutionContext): List[ActionObjectInfo] =
    execute(arguments(0).asInstanceOf[TI_0], arguments(1).asInstanceOf[TI_1])(context)

  def execute(t0: TI_0, t1: TI_1)(context: ExecutionContext): (TO_0, TO_1)

  final def inferKnowledgeUntyped(knowledge: List[Knowledge[ActionObjectInfo]])(context: InferContext) =
    inferKnowledge(knowledge(0).asInstanceOf[Knowledge[TI_0]], knowledge(1).asInstanceOf[Knowledge[TI_1]])(context)

  def inferKnowledge(k0: Knowledge[TI_0], k1: Knowledge[TI_1])(context: InferContext):
    ((Knowledge[TO_0], Knowledge[TO_1]), InferenceWarnings) = {
    (
      (
        Knowledge(context.actionObjectCatalog.concreteSubclassesInstances[TO_0](tTagTO_0)),
        Knowledge(context.actionObjectCatalog.concreteSubclassesInstances[TO_1](tTagTO_1))
      ),
      InferenceWarnings.empty
    )
  }

  val tTagTO_0: Tag[TO_0]
  val tTagTO_1: Tag[TO_1]
}

trait ActionTypeType2To3[
    TI_0 <: ActionObjectInfo,
    TI_1 <: ActionObjectInfo,
    TO_0 <: ActionObjectInfo,
    TO_1 <: ActionObjectInfo,
    TO_2 <: ActionObjectInfo
] extends ActionType {

  final def executeUntyped(arguments: List[ActionObjectInfo])(context: ExecutionContext): List[ActionObjectInfo] =
    execute(arguments(0).asInstanceOf[TI_0], arguments(1).asInstanceOf[TI_1])(context)

  def execute(t0: TI_0, t1: TI_1)(context: ExecutionContext): (TO_0, TO_1, TO_2)

  final def inferKnowledgeUntyped(knowledge: List[Knowledge[ActionObjectInfo]])(context: InferContext) =
    inferKnowledge(knowledge(0).asInstanceOf[Knowledge[TI_0]], knowledge(1).asInstanceOf[Knowledge[TI_1]])(context)

  def inferKnowledge(k0: Knowledge[TI_0], k1: Knowledge[TI_1])(context: InferContext): ((Knowledge[TO_0], Knowledge[TO_1], Knowledge[TO_2]), InferenceWarnings) = {
    (
      (
        Knowledge(context.actionObjectCatalog.concreteSubclassesInstances[TO_0](tTagTO_0)),
        Knowledge(context.actionObjectCatalog.concreteSubclassesInstances[TO_1](tTagTO_1)),
        Knowledge(context.actionObjectCatalog.concreteSubclassesInstances[TO_2](tTagTO_2))
      ),
      InferenceWarnings.empty
    )
  }

  val tTagTO_0: Tag[TO_0]
  val tTagTO_1: Tag[TO_1]
  val tTagTO_2: Tag[TO_2]
}

trait ActionTypeType3To0[
  TI_0 <: ActionObjectInfo,
  TI_1 <: ActionObjectInfo,
  TI_2 <: ActionObjectInfo] extends ActionType {

  final def executeUntyped(arguments: List[ActionObjectInfo])(context: ExecutionContext): List[ActionObjectInfo] = {
    execute(arguments(0).asInstanceOf[TI_0], arguments(1).asInstanceOf[TI_1], arguments(2).asInstanceOf[TI_2])(context)
    List.empty
  }

  def execute(t0: TI_0, t1: TI_1, t2: TI_2)(context: ExecutionContext): Unit

  final def inferKnowledgeUntyped(knowledge: List[Knowledge[ActionObjectInfo]])(context: InferContext) = {
    inferKnowledge(
      knowledge(0).asInstanceOf[Knowledge[TI_0]],
      knowledge(1).asInstanceOf[Knowledge[TI_1]],
      knowledge(2).asInstanceOf[Knowledge[TI_2]]
    )(context)
    (List.empty, InferenceWarnings.empty)
  }

  def inferKnowledge(k0: Knowledge[TI_0], k1: Knowledge[TI_1], k2: Knowledge[TI_2])(context: InferContext): (Unit, InferenceWarnings) = ((), InferenceWarnings.empty)
}

trait ActionTypeType3To1[
  TI_0 <: ActionObjectInfo,
  TI_1 <: ActionObjectInfo,
  TI_2 <: ActionObjectInfo,
  TO_0 <: ActionObjectInfo] extends ActionType {

  final def executeUntyped(arguments: List[ActionObjectInfo])(context: ExecutionContext): List[ActionObjectInfo] =
    execute(arguments(0).asInstanceOf[TI_0], arguments(1).asInstanceOf[TI_1], arguments(2).asInstanceOf[TI_2])(context)

  def execute(t0: TI_0, t1: TI_1, t2: TI_2)(context: ExecutionContext): TO_0

  final def inferKnowledgeUntyped(knowledge: List[Knowledge[ActionObjectInfo]])(context: InferContext): (List[Knowledge[ActionObjectInfo]], InferenceWarnings) = {
    inferKnowledge(
      knowledge(0).asInstanceOf[Knowledge[TI_0]],
      knowledge(1).asInstanceOf[Knowledge[TI_1]],
      knowledge(2).asInstanceOf[Knowledge[TI_2]]
    )(context)
  }

  def inferKnowledge(k0: Knowledge[TI_0], k1: Knowledge[TI_1], k2: Knowledge[TI_2])(context: InferContext): (Knowledge[TO_0], InferenceWarnings) =
    (Knowledge(context.actionObjectCatalog.concreteSubclassesInstances[TO_0](tTagTO_0)), InferenceWarnings.empty)

  val tTagTO_0: Tag[TO_0]
}

trait ActionTypeType3To2[
    TI_0 <: ActionObjectInfo,
    TI_1 <: ActionObjectInfo,
    TI_2 <: ActionObjectInfo,
    TO_0 <: ActionObjectInfo,
    TO_1 <: ActionObjectInfo
] extends ActionType {

  final def executeUntyped(arguments: List[ActionObjectInfo])(context: ExecutionContext): List[ActionObjectInfo] =
    execute(arguments(0).asInstanceOf[TI_0], arguments(1).asInstanceOf[TI_1], arguments(2).asInstanceOf[TI_2])(context)

  def execute(t0: TI_0, t1: TI_1, t2: TI_2)(context: ExecutionContext): (TO_0, TO_1)

  final def inferKnowledgeUntyped(knowledge: List[Knowledge[ActionObjectInfo]])(context: InferContext) =
    inferKnowledge(
      knowledge(0).asInstanceOf[Knowledge[TI_0]],
      knowledge(1).asInstanceOf[Knowledge[TI_1]],
      knowledge(2).asInstanceOf[Knowledge[TI_2]]
    )(context)

  def inferKnowledge(k0: Knowledge[TI_0], k1: Knowledge[TI_1], k2: Knowledge[TI_2])(context: InferContext): ((Knowledge[TO_0], Knowledge[TO_1]), InferenceWarnings) = {
    (
      (
        Knowledge(context.actionObjectCatalog.concreteSubclassesInstances[TO_0](tTagTO_0)),
        Knowledge(context.actionObjectCatalog.concreteSubclassesInstances[TO_1](tTagTO_1))
      ),
      InferenceWarnings.empty
    )
  }

  val tTagTO_0: Tag[TO_0]
  val tTagTO_1: Tag[TO_1]
}

trait ActionTypeType3To3[
    TI_0 <: ActionObjectInfo,
    TI_1 <: ActionObjectInfo,
    TI_2 <: ActionObjectInfo,
    TO_0 <: ActionObjectInfo,
    TO_1 <: ActionObjectInfo,
    TO_2 <: ActionObjectInfo
] extends ActionType {

  final def executeUntyped(arguments: List[ActionObjectInfo])(context: ExecutionContext): List[ActionObjectInfo] =
    execute(arguments(0).asInstanceOf[TI_0], arguments(1).asInstanceOf[TI_1], arguments(2).asInstanceOf[TI_2])(context)

  def execute(t0: TI_0, t1: TI_1, t2: TI_2)(context: ExecutionContext): (TO_0, TO_1, TO_2)

  final def inferKnowledgeUntyped(knowledge: List[Knowledge[ActionObjectInfo]])(context: InferContext) =
    inferKnowledge(
      knowledge(0).asInstanceOf[Knowledge[TI_0]],
      knowledge(1).asInstanceOf[Knowledge[TI_1]],
      knowledge(2).asInstanceOf[Knowledge[TI_2]]
    )(context)

  def inferKnowledge(k0: Knowledge[TI_0], k1: Knowledge[TI_1], k2: Knowledge[TI_2])(context: InferContext): ((Knowledge[TO_0], Knowledge[TO_1], Knowledge[TO_2]), InferenceWarnings) = {
    (
      (
        Knowledge(context.actionObjectCatalog.concreteSubclassesInstances[TO_0](tTagTO_0)),
        Knowledge(context.actionObjectCatalog.concreteSubclassesInstances[TO_1](tTagTO_1)),
        Knowledge(context.actionObjectCatalog.concreteSubclassesInstances[TO_2](tTagTO_2))
      ),
      InferenceWarnings.empty
    )
  }

  val tTagTO_0: Tag[TO_0]
  val tTagTO_1: Tag[TO_1]
  val tTagTO_2: Tag[TO_2]
}