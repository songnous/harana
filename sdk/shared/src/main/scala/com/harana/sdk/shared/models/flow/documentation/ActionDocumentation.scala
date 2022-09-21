package com.harana.sdk.shared.models.flow.documentation

import com.harana.sdk.shared.models.flow.ActionTypeInfo

trait ActionDocumentation extends Documentable { self: ActionTypeInfo =>

  final override def hasDocumentation: Boolean = true

}
