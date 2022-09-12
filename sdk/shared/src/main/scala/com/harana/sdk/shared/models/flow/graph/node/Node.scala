package com.harana.sdk.shared.models.flow.graph.node

import com.harana.sdk.shared.models.flow.utils

case class Node[+T](id: Node.Id, value: T)

object Node {

  type Id = utils.Id
  val Id = utils.Id

}
