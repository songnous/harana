package com.harana.sdk.shared.models.flow.graph.node

import com.harana.sdk.shared.models.flow.utils
import io.circe.generic.JsonCodec

case class Node[+T](id: Node.Id, value: T)

object Node {

  type Id = utils.Id
  val Id = utils.Id

}