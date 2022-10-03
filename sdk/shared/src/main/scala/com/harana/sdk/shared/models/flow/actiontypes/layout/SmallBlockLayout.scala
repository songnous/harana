package com.harana.sdk.shared.models.flow.actiontypes.layout

import com.harana.sdk.shared.models.flow.{Action0To2TypeInfo, Action1To2TypeInfo, Action2To0TypeInfo, Action2To1TypeInfo, Action2To2TypeInfo, Action2To3TypeInfo, Action3To2TypeInfo, PortPosition}

sealed trait SmallBlockLayout {
  val symmetricPortLayout = List(PortPosition.Left, PortPosition.Right)
}

trait SmallBlockLayout2To0 extends SmallBlockLayout { self: Action2To0TypeInfo[_, _] =>
  override val inputPortsLayout: List[PortPosition] = symmetricPortLayout
}

trait SmallBlockLayout2To1 extends SmallBlockLayout { self: Action2To1TypeInfo[_, _, _] =>
  override val inputPortsLayout: List[PortPosition] = symmetricPortLayout
}

trait SmallBlockLayout2To2 extends SmallBlockLayout { self: Action2To2TypeInfo[_, _, _, _] =>
  override val inputPortsLayout: List[PortPosition] = symmetricPortLayout
  override def outputPortsLayout: List[PortPosition] = symmetricPortLayout
}

trait SmallBlockLayout2To3 extends SmallBlockLayout { self: Action2To3TypeInfo[_, _, _, _, _] =>
  override val inputPortsLayout: List[PortPosition] = symmetricPortLayout
}

trait SmallBlockLayout0To2 extends SmallBlockLayout { self: Action0To2TypeInfo[_, _] =>
  override def outputPortsLayout: List[PortPosition] = symmetricPortLayout
}

trait SmallBlockLayout1To2 extends SmallBlockLayout { self: Action1To2TypeInfo[_, _, _] =>
  override def outputPortsLayout: List[PortPosition] = symmetricPortLayout
}

trait SmallBlockLayout3To2 extends SmallBlockLayout { self: Action3To2TypeInfo[_, _, _, _, _] =>
  override def outputPortsLayout: List[PortPosition] = symmetricPortLayout
}