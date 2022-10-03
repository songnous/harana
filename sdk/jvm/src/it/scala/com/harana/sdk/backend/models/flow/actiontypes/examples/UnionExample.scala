package com.harana.sdk.backend.models.flow.actiontypes.examples

import com.harana.sdk.backend.models.flow.actiontypes.Union

class UnionExample extends AbstractActionExample[Union] {

  def action: Union = Union()

  override def fileNames = Seq("example_union1", "example_union2")

}
