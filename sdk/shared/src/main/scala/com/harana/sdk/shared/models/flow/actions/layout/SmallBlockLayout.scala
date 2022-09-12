package com.harana.sdk.shared.models.flow.actions.layout

import com.harana.sdk.shared.models.flow.{Action0To2Info, Action1To2Info, Action2To0Info, Action2To1Info, Action2To2Info, Action2To3Info, Action3To2Info, PortPosition}

sealed trait SmallBlockLayout {
  val symmetricPortLayout = Vector(PortPosition.Left, PortPosition.Right)
}

trait SmallBlockLayout2To0 extends SmallBlockLayout { self: Action2To0Info[_, _] =>
  override val inPortsLayout: Vector[PortPosition] = symmetricPortLayout
}

trait SmallBlockLayout2To1 extends SmallBlockLayout { self: Action2To1Info[_, _, _] =>
  override val inPortsLayout: Vector[PortPosition] = symmetricPortLayout
}

trait SmallBlockLayout2To2 extends SmallBlockLayout { self: Action2To2Info[_, _, _, _] =>
  override val inPortsLayout: Vector[PortPosition] = symmetricPortLayout
  override def outPortsLayout: Vector[PortPosition] = symmetricPortLayout
}

trait SmallBlockLayout2To3 extends SmallBlockLayout { self: Action2To3Info[_, _, _, _, _] =>
  override val inPortsLayout: Vector[PortPosition] = symmetricPortLayout
}

trait SmallBlockLayout0To2 extends SmallBlockLayout { self: Action0To2Info[_, _] =>
  override def outPortsLayout: Vector[PortPosition] = symmetricPortLayout
}

trait SmallBlockLayout1To2 extends SmallBlockLayout { self: Action1To2Info[_, _, _] =>
  override def outPortsLayout: Vector[PortPosition] = symmetricPortLayout
}

trait SmallBlockLayout3To2 extends SmallBlockLayout { self: Action3To2Info[_, _, _, _, _] =>
  override def outPortsLayout: Vector[PortPosition] = symmetricPortLayout
}