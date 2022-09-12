package com.harana.sdk.shared.plugin

import com.harana.sdk.shared.models.common.Parameter.ParameterName
import com.harana.sdk.shared.models.common.{Layout, ParameterValue}
import com.harana.sdk.shared.plugin.PanelType.PanelTypeId
import com.harana.sdk.shared.models.common.{Layout, ParameterValue}

trait PageType extends Service {

  def detailLayout(parameterValues: Map[ParameterName, ParameterValue]): Layout

  def listLayout(parameterValues: Map[ParameterName, ParameterValue]): Layout

  def linkedPanelTypes: Set[PanelTypeId] = Set.empty

}

object PageType {
  type PageTypeId = String
}