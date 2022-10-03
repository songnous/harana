package com.harana.sdk.backend.models.flow

import com.harana.sdk.backend.models.flow.inference.{InferContext, InferenceWarnings}
import com.harana.sdk.shared.models.flow.actionobjects.ActionObjectInfo

import scala.reflect.runtime.{universe => ru}

// code below is generated automatically
// scalastyle:off
abstract class Method0To1[P, +TO_0 <: ActionObjectInfo: ru.TypeTag] extends Method {

  def apply(context: ExecutionContext)(parameters: P)(): TO_0

  def infer(context: InferContext)(parameters: P)(): (Knowledge[TO_0], InferenceWarnings) =
    (Knowledge(context.actionObjectCatalog.concreteSubclassesInstances[TO_0]), InferenceWarnings.empty)

}

abstract class Method0To2[P, +TO_0 <: ActionObjectInfo: ru.TypeTag, +TO_1 <: ActionObjectInfo: ru.TypeTag] extends Method {

  def apply(context: ExecutionContext)(parameters: P)(): (TO_0, TO_1)

  def infer(context: InferContext)(parameters: P)(): ((Knowledge[TO_0], Knowledge[TO_1]), InferenceWarnings) = {
    (
      (
        Knowledge(context.actionObjectCatalog.concreteSubclassesInstances[TO_0]),
        Knowledge(context.actionObjectCatalog.concreteSubclassesInstances[TO_1])
      ),
      InferenceWarnings.empty
    )
  }
}

abstract class Method0To3[
    P,
    +TO_0 <: ActionObjectInfo: ru.TypeTag,
    +TO_1 <: ActionObjectInfo: ru.TypeTag,
    +TO_2 <: ActionObjectInfo: ru.TypeTag
] extends Method {

  def apply(context: ExecutionContext)(parameters: P)(): (TO_0, TO_1, TO_2)

  def infer(
      context: InferContext
  )(parameters: P)(): ((Knowledge[TO_0], Knowledge[TO_1], Knowledge[TO_2]), InferenceWarnings) = {
    (
      (
        Knowledge(context.actionObjectCatalog.concreteSubclassesInstances[TO_0]),
        Knowledge(context.actionObjectCatalog.concreteSubclassesInstances[TO_1]),
        Knowledge(context.actionObjectCatalog.concreteSubclassesInstances[TO_2])
      ),
      InferenceWarnings.empty
    )
  }
}

abstract class Method1To0[P, TI_0 <: ActionObjectInfo: ru.TypeTag] extends Method {

  def apply(context: ExecutionContext)(parameters: P)(t0: TI_0): Unit

  def infer(context: InferContext)(parameters: P)(k0: Knowledge[TI_0]): (Unit, InferenceWarnings) =
    ((), InferenceWarnings.empty)

}

abstract class Method1To1[P, TI_0 <: ActionObjectInfo: ru.TypeTag, +TO_0 <: ActionObjectInfo: ru.TypeTag] extends Method {

  def apply(context: ExecutionContext)(parameters: P)(t0: TI_0): TO_0

  def infer(context: InferContext)(parameters: P)(k0: Knowledge[TI_0]): (Knowledge[TO_0], InferenceWarnings) =
    (Knowledge(context.actionObjectCatalog.concreteSubclassesInstances[TO_0]), InferenceWarnings.empty)

}

abstract class Method1To2[
    P,
    TI_0 <: ActionObjectInfo: ru.TypeTag,
    +TO_0 <: ActionObjectInfo: ru.TypeTag,
    +TO_1 <: ActionObjectInfo: ru.TypeTag
] extends Method {

  def apply(context: ExecutionContext)(parameters: P)(t0: TI_0): (TO_0, TO_1)

  def infer(
      context: InferContext
  )(parameters: P)(k0: Knowledge[TI_0]): ((Knowledge[TO_0], Knowledge[TO_1]), InferenceWarnings) = {
    (
      (
        Knowledge(context.actionObjectCatalog.concreteSubclassesInstances[TO_0]),
        Knowledge(context.actionObjectCatalog.concreteSubclassesInstances[TO_1])
      ),
      InferenceWarnings.empty
    )
  }
}

abstract class Method1To3[
    P,
    TI_0 <: ActionObjectInfo: ru.TypeTag,
    +TO_0 <: ActionObjectInfo: ru.TypeTag,
    +TO_1 <: ActionObjectInfo: ru.TypeTag,
    +TO_2 <: ActionObjectInfo: ru.TypeTag
] extends Method {

  def apply(context: ExecutionContext)(parameters: P)(t0: TI_0): (TO_0, TO_1, TO_2)

  def infer(context: InferContext)(
      parameters: P
  )(k0: Knowledge[TI_0]): ((Knowledge[TO_0], Knowledge[TO_1], Knowledge[TO_2]), InferenceWarnings) = {
    (
      (
        Knowledge(context.actionObjectCatalog.concreteSubclassesInstances[TO_0]),
        Knowledge(context.actionObjectCatalog.concreteSubclassesInstances[TO_1]),
        Knowledge(context.actionObjectCatalog.concreteSubclassesInstances[TO_2])
      ),
      InferenceWarnings.empty
    )
  }
}

abstract class Method2To0[P, TI_0 <: ActionObjectInfo: ru.TypeTag, TI_1 <: ActionObjectInfo: ru.TypeTag] extends Method {

  def apply(context: ExecutionContext)(parameters: P)(t0: TI_0, t1: TI_1): Unit

  def infer(context: InferContext)(
      parameters: P
  )(k0: Knowledge[TI_0], k1: Knowledge[TI_1]): (Unit, InferenceWarnings) = ((), InferenceWarnings.empty)

}

abstract class Method2To1[
    P,
    TI_0 <: ActionObjectInfo: ru.TypeTag,
    TI_1 <: ActionObjectInfo: ru.TypeTag,
    +TO_0 <: ActionObjectInfo: ru.TypeTag
] extends Method {

  def apply(context: ExecutionContext)(parameters: P)(t0: TI_0, t1: TI_1): TO_0

  def infer(
      context: InferContext
  )(parameters: P)(k0: Knowledge[TI_0], k1: Knowledge[TI_1]): (Knowledge[TO_0], InferenceWarnings) =
    (Knowledge(context.actionObjectCatalog.concreteSubclassesInstances[TO_0]), InferenceWarnings.empty)

}

abstract class Method2To2[
    P,
    TI_0 <: ActionObjectInfo: ru.TypeTag,
    TI_1 <: ActionObjectInfo: ru.TypeTag,
    +TO_0 <: ActionObjectInfo: ru.TypeTag,
    +TO_1 <: ActionObjectInfo: ru.TypeTag
] extends Method {

  def apply(context: ExecutionContext)(parameters: P)(t0: TI_0, t1: TI_1): (TO_0, TO_1)

  def infer(context: InferContext)(
      parameters: P
  )(k0: Knowledge[TI_0], k1: Knowledge[TI_1]): ((Knowledge[TO_0], Knowledge[TO_1]), InferenceWarnings) = {
    (
      (
        Knowledge(context.actionObjectCatalog.concreteSubclassesInstances[TO_0]),
        Knowledge(context.actionObjectCatalog.concreteSubclassesInstances[TO_1])
      ),
      InferenceWarnings.empty
    )
  }
}

abstract class Method2To3[
    P,
    TI_0 <: ActionObjectInfo: ru.TypeTag,
    TI_1 <: ActionObjectInfo: ru.TypeTag,
    +TO_0 <: ActionObjectInfo: ru.TypeTag,
    +TO_1 <: ActionObjectInfo: ru.TypeTag,
    +TO_2 <: ActionObjectInfo: ru.TypeTag
] extends Method {

  def apply(context: ExecutionContext)(parameters: P)(t0: TI_0, t1: TI_1): (TO_0, TO_1, TO_2)

  def infer(context: InferContext)(parameters: P)(
    k0: Knowledge[TI_0],
    k1: Knowledge[TI_1]
  ): ((Knowledge[TO_0], Knowledge[TO_1], Knowledge[TO_2]), InferenceWarnings) = {
    (
      (
        Knowledge(context.actionObjectCatalog.concreteSubclassesInstances[TO_0]),
        Knowledge(context.actionObjectCatalog.concreteSubclassesInstances[TO_1]),
        Knowledge(context.actionObjectCatalog.concreteSubclassesInstances[TO_2])
      ),
      InferenceWarnings.empty
    )
  }
}

abstract class Method3To0[
    P,
    TI_0 <: ActionObjectInfo: ru.TypeTag,
    TI_1 <: ActionObjectInfo: ru.TypeTag,
    TI_2 <: ActionObjectInfo: ru.TypeTag
] extends Method {

  def apply(context: ExecutionContext)(parameters: P)(t0: TI_0, t1: TI_1, t2: TI_2): Unit

  def infer(context: InferContext)(parameters: P)(k0: Knowledge[TI_0], k1: Knowledge[TI_1], k2: Knowledge[TI_2]): (Unit, InferenceWarnings) =
    ((), InferenceWarnings.empty)

}

abstract class Method3To1[
    P,
    TI_0 <: ActionObjectInfo: ru.TypeTag,
    TI_1 <: ActionObjectInfo: ru.TypeTag,
    TI_2 <: ActionObjectInfo: ru.TypeTag,
    +TO_0 <: ActionObjectInfo: ru.TypeTag
] extends Method {

  def apply(context: ExecutionContext)(parameters: P)(t0: TI_0, t1: TI_1, t2: TI_2): TO_0

  def infer(context: InferContext)(parameters: P)(k0: Knowledge[TI_0], k1: Knowledge[TI_1], k2: Knowledge[TI_2]): (Knowledge[TO_0], InferenceWarnings) =
    (Knowledge(context.actionObjectCatalog.concreteSubclassesInstances[TO_0]), InferenceWarnings.empty)

}

abstract class Method3To2[
    P,
    TI_0 <: ActionObjectInfo: ru.TypeTag,
    TI_1 <: ActionObjectInfo: ru.TypeTag,
    TI_2 <: ActionObjectInfo: ru.TypeTag,
    +TO_0 <: ActionObjectInfo: ru.TypeTag,
    +TO_1 <: ActionObjectInfo: ru.TypeTag
] extends Method {

  def apply(context: ExecutionContext)(parameters: P)(t0: TI_0, t1: TI_1, t2: TI_2): (TO_0, TO_1)

  def infer(context: InferContext)(parameters: P)(
    k0: Knowledge[TI_0],
    k1: Knowledge[TI_1],
    k2: Knowledge[TI_2]
  ): ((Knowledge[TO_0], Knowledge[TO_1]), InferenceWarnings) = {
    (
      (
        Knowledge(context.actionObjectCatalog.concreteSubclassesInstances[TO_0]),
        Knowledge(context.actionObjectCatalog.concreteSubclassesInstances[TO_1])
      ),
      InferenceWarnings.empty
    )
  }
}

abstract class Method3To3[
    P,
    TI_0 <: ActionObjectInfo: ru.TypeTag,
    TI_1 <: ActionObjectInfo: ru.TypeTag,
    TI_2 <: ActionObjectInfo: ru.TypeTag,
    +TO_0 <: ActionObjectInfo: ru.TypeTag,
    +TO_1 <: ActionObjectInfo: ru.TypeTag,
    +TO_2 <: ActionObjectInfo: ru.TypeTag
] extends Method {

  def apply(context: ExecutionContext)(parameters: P)(t0: TI_0, t1: TI_1, t2: TI_2): (TO_0, TO_1, TO_2)

  def infer(context: InferContext)(parameters: P)(
    k0: Knowledge[TI_0],
    k1: Knowledge[TI_1],
    k2: Knowledge[TI_2]
  ): ((Knowledge[TO_0], Knowledge[TO_1], Knowledge[TO_2]), InferenceWarnings) = {
    (
      (
        Knowledge(context.actionObjectCatalog.concreteSubclassesInstances[TO_0]),
        Knowledge(context.actionObjectCatalog.concreteSubclassesInstances[TO_1]),
        Knowledge(context.actionObjectCatalog.concreteSubclassesInstances[TO_2])
      ),
      InferenceWarnings.empty
    )
  }
}

// scalastyle:on
