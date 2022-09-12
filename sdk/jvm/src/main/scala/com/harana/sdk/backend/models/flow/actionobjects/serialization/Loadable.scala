package com.harana.sdk.backend.models.flow.actionobjects.serialization

import com.harana.sdk.backend.models.flow.ExecutionContext

trait Loadable {
  def load(ctx: ExecutionContext, path: String): this.type
}
