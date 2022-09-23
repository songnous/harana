package com.harana.sdk.shared.models.flow.documentation

import com.harana.sdk.shared.models.flow.ActionInfo

trait ActionDocumentation extends Documentable { self: ActionInfo =>

  final override def hasDocumentation: Boolean = true

}
