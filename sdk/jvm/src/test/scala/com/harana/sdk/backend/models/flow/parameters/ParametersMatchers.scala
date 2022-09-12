package com.harana.sdk.backend.models.flow.parameters

import com.harana.sdk.shared.models.flow.parameters.Parameters
import org.scalatest.matchers.HavePropertyMatchResult
import org.scalatest.matchers.HavePropertyMatcher

object ParametersMatchers {

  def theSameParametersAs(right: Parameters) =
    (left: Parameters) => HavePropertyMatchResult(left.sameAs(right), "param values", right, left)
}
