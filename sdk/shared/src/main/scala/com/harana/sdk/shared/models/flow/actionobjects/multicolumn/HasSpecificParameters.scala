package com.harana.sdk.shared.models.flow.actionobjects.multicolumn

import com.harana.sdk.shared.models.flow.parameters.{Parameter, Parameters}

trait HasSpecificParameters { self: Parameters =>

  val specificParameters: Array[Parameter[_]]

}
