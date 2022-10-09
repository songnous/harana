package com.harana.sdk.shared.models.flow

import com.harana.sdk.shared.models.flow.actionobjects.ActionObjectInfo
import izumi.reflect.Tag

import scala.language.implicitConversions
import scala.reflect.runtime.{universe => ru}

object ActionTypeConversions {
  implicit def singleValueToList[T1 <: ActionObjectInfo](t: T1) = List(t)
  implicit def tuple2ToList[T1 <: ActionObjectInfo, T2 <: ActionObjectInfo](t: (T1, T2)) = List(t._1, t._2)
  implicit def tuple3ToList[T1 <: ActionObjectInfo, T2 <: ActionObjectInfo, T3 <: ActionObjectInfo](t: (T1, T2, T3)) = List(t._1, t._2, t._3)
}

trait Action0To1TypeInfo[TO_0 <: ActionObjectInfo] extends ActionTypeInfo {

  final val inArity = 0
  final val outArity = 1

  def portO_0: Tag[TO_0]

  @transient
  final override lazy val inputPorts: List[Tag[_]] = List()

  @transient
  final override lazy val outputPorts: List[Tag[_]] = List(Tag[TO_0](portO_0))

}

trait Action0To2TypeInfo[TO_0 <: ActionObjectInfo, TO_1 <: ActionObjectInfo] extends ActionTypeInfo {

  final val inArity = 0
  final val outArity = 2

  def portO_0: Tag[TO_0]
  def portO_1: Tag[TO_1]

  @transient
  final override lazy val inputPorts: List[Tag[_]] = List()

  @transient
  final override lazy val outputPorts: List[Tag[_]] =
    List(Tag[TO_0](portO_0), Tag[TO_1](portO_1))

}

trait Action0To3TypeInfo[TO_0 <: ActionObjectInfo, TO_1 <: ActionObjectInfo, TO_2 <: ActionObjectInfo] extends ActionTypeInfo {

  final val inArity = 0
  final val outArity = 3

  def portO_0: Tag[TO_0]
  def portO_1: Tag[TO_1]
  def portO_2: Tag[TO_2]

  @transient
  final override lazy val inputPorts: List[Tag[_]] = List()

  @transient
  final override lazy val outputPorts: List[Tag[_]] =
    List(Tag[TO_0](portO_0), Tag[TO_1](portO_1), Tag[TO_2](portO_2))

}

trait Action1To0TypeInfo[TI_0 <: ActionObjectInfo] extends ActionTypeInfo {

  final val inArity = 1
  final val outArity = 0

  def portI_0: Tag[TI_0]

  @transient
  final override lazy val inputPorts: List[Tag[_]] = List(Tag[TI_0](portI_0))

  @transient
  final override lazy val outputPorts: List[Tag[_]] = List()

}

trait Action1To1TypeInfo[TI_0 <: ActionObjectInfo, TO_0 <: ActionObjectInfo] extends ActionTypeInfo {

  final val inArity = 1
  final val outArity = 1

  def portI_0: Tag[TI_0]
  def portO_0: Tag[TO_0]

  @transient
  final override lazy val inputPorts: List[Tag[_]] = List(Tag[TI_0](portI_0))

  @transient
  final override lazy val outputPorts: List[Tag[_]] = List(Tag[TO_0](portO_0))

}

trait Action1To2TypeInfo[TI_0 <: ActionObjectInfo, TO_0 <: ActionObjectInfo, TO_1 <: ActionObjectInfo] extends ActionTypeInfo {

  final val inArity = 1
  final val outArity = 2

  def portI_0: Tag[TI_0]
  def portO_0: Tag[TO_0]
  def portO_1: Tag[TO_1]

  @transient
  final override lazy val inputPorts: List[Tag[_]] = List(Tag[TI_0](portI_0))

  @transient
  final override lazy val outputPorts: List[Tag[_]] =
    List(Tag[TO_0](portO_0), Tag[TO_1](portO_1))

}

trait Action1To3TypeInfo[TI_0 <: ActionObjectInfo, TO_0 <: ActionObjectInfo, TO_1 <: ActionObjectInfo, TO_2 <: ActionObjectInfo] extends ActionTypeInfo {

  final val inArity = 1
  final val outArity = 3

  def portI_0: Tag[TI_0]
  def portO_0: Tag[TO_0]
  def portO_1: Tag[TO_1]
  def portO_2: Tag[TO_2]

  @transient
  final override lazy val inputPorts: List[Tag[_]] = List(Tag[TI_0](portI_0))

  @transient
  final override lazy val outputPorts: List[Tag[_]] =
    List(Tag[TO_0](portO_0), Tag[TO_1](portO_1), Tag[TO_2](portO_2))

}

trait Action2To0TypeInfo[TI_0 <: ActionObjectInfo, TI_1 <: ActionObjectInfo] extends ActionTypeInfo {
  final val inArity = 2
  final val outArity = 0

  def portI_0: Tag[TI_0]
  def portI_1: Tag[TI_1]

  @transient
  final override lazy val inputPorts: List[Tag[_]] =
    List(Tag[TI_0](portI_0), Tag[TI_1](portI_1))

  @transient
  final override lazy val outputPorts: List[Tag[_]] = List()

}

trait Action2To1TypeInfo[TI_0 <: ActionObjectInfo, TI_1 <: ActionObjectInfo, TO_0 <: ActionObjectInfo] extends ActionTypeInfo {

  final val inArity = 2
  final val outArity = 1

  def portI_0: Tag[TI_0]
  def portI_1: Tag[TI_1]
  def portO_0: Tag[TO_0]

  @transient
  final override lazy val inputPorts: List[Tag[_]] =
    List(Tag[TI_0](portI_0), Tag[TI_1](portI_1))

  @transient
  final override lazy val outputPorts: List[Tag[_]] = List(Tag[TO_0](portO_0))

}

trait Action2To2TypeInfo[TI_0 <: ActionObjectInfo, TI_1 <: ActionObjectInfo, TO_0 <: ActionObjectInfo, TO_1 <: ActionObjectInfo] extends ActionTypeInfo {

  final val inArity = 2
  final val outArity = 2

  def portI_0: Tag[TI_0]
  def portI_1: Tag[TI_1]
  def portO_0: Tag[TO_0]
  def portO_1: Tag[TO_1]

  @transient
  final override lazy val inputPorts: List[Tag[_]] =
    List(Tag[TI_0](portI_0), Tag[TI_1](portI_1))

  @transient
  final override lazy val outputPorts: List[Tag[_]] =
    List(Tag[TO_0](portO_0), Tag[TO_1](portO_1))

}

trait Action2To3TypeInfo[
  TI_0 <: ActionObjectInfo,
  TI_1 <: ActionObjectInfo,
  TO_0 <: ActionObjectInfo,
  TO_1 <: ActionObjectInfo,
  TO_2 <: ActionObjectInfo
] extends ActionTypeInfo {

  final val inArity = 2

  final val outArity = 3

  def portI_0: Tag[TI_0]
  def portI_1: Tag[TI_1]
  def portO_0: Tag[TO_0]
  def portO_1: Tag[TO_1]
  def portO_2: Tag[TO_2]

  @transient
  final override lazy val inputPorts: List[Tag[_]] =
    List(Tag[TI_0](portI_0), Tag[TI_1](portI_1))

  @transient
  final override lazy val outputPorts: List[Tag[_]] =
    List(Tag[TO_0](portO_0), Tag[TO_1](portO_1), Tag[TO_2](portO_2))

}

trait Action3To0TypeInfo[TI_0 <: ActionObjectInfo, TI_1 <: ActionObjectInfo, TI_2 <: ActionObjectInfo] extends ActionTypeInfo {

  final val inArity = 3
  final val outArity = 0

  def portI_0: Tag[TI_0]
  def portI_1: Tag[TI_1]
  def portI_2: Tag[TI_2]

  @transient
  final override lazy val inputPorts: List[Tag[_]] =
    List(Tag[TI_0](portI_0), Tag[TI_1](portI_1), Tag[TI_2](portI_2))

  @transient
  final override lazy val outputPorts: List[Tag[_]] = List()

}

trait Action3To1TypeInfo[TI_0 <: ActionObjectInfo, TI_1 <: ActionObjectInfo, TI_2 <: ActionObjectInfo, TO_0 <: ActionObjectInfo] extends ActionTypeInfo {
  final val inArity = 3
  final val outArity = 1

  def portI_0: Tag[TI_0]
  def portI_1: Tag[TI_1]
  def portI_2: Tag[TI_2]
  def portO_0: Tag[TO_0]

  @transient
  final override lazy val inputPorts: List[Tag[_]] =
    List(Tag[TI_0](portI_0), Tag[TI_1](portI_1), Tag[TI_2](portI_2))

  @transient
  final override lazy val outputPorts: List[Tag[_]] = List(Tag[TO_0](portO_0))

}

trait Action3To2TypeInfo[
  TI_0 <: ActionObjectInfo,
  TI_1 <: ActionObjectInfo,
  TI_2 <: ActionObjectInfo,
  TO_0 <: ActionObjectInfo,
  TO_1 <: ActionObjectInfo
] extends ActionTypeInfo {

  final val inArity = 3
  final val outArity = 2

  def portI_0: Tag[TI_0]
  def portI_1: Tag[TI_1]
  def portI_2: Tag[TI_2]
  def portO_0: Tag[TO_0]
  def portO_1: Tag[TO_1]

  @transient
  final override lazy val inputPorts: List[Tag[_]] =
    List(Tag[TI_0](portI_0), Tag[TI_1](portI_1), Tag[TI_2](portI_2))

  @transient
  final override lazy val outputPorts: List[Tag[_]] =
    List(Tag[TO_0](portO_0), Tag[TO_1](portO_1))

}

trait Action3To3TypeInfo[
  TI_0 <: ActionObjectInfo,
  TI_1 <: ActionObjectInfo,
  TI_2 <: ActionObjectInfo,
  TO_0 <: ActionObjectInfo,
  TO_1 <: ActionObjectInfo,
  TO_2 <: ActionObjectInfo
] extends ActionTypeInfo {

  final val outArity = 3

  def portI_0: Tag[TI_0]
  def portI_1: Tag[TI_1]
  def portI_2: Tag[TI_2]
  def portO_0: Tag[TO_0]
  def portO_1: Tag[TO_1]
  def portO_2: Tag[TO_2]

  @transient
  final override lazy val inputPorts: List[Tag[_]] =
    List(Tag[TI_0](portI_0), Tag[TI_1](portI_1), Tag[TI_2](portI_2))

  @transient
  final override lazy val outputPorts: List[Tag[_]] =
    List(Tag[TO_0](portO_0), Tag[TO_1](portO_1), Tag[TO_2](portO_2))
}