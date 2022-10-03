package com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.parameters.common

import com.harana.sdk.shared.models.flow.parameters.selections.{NameSingleColumnSelection, SingleColumnSelection}
import com.harana.sdk.shared.models.flow.parameters.{Parameters, SingleColumnSelectorParameter}

import scala.language.reflectiveCalls

trait HasFeaturesColumnParameter extends Parameters {

  val featuresColumnParameter = SingleColumnSelectorParameter("features-column", default = Some(NameSingleColumnSelection("features")), portIndex = 0)
  def setFeaturesColumn(value: SingleColumnSelection): this.type = set(featuresColumnParameter, value)

}