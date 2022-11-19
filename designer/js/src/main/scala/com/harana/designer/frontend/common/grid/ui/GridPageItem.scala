
package com.harana.designer.frontend.common.grid.ui

import com.harana.sdk.shared.models.common.Background
import com.harana.sdk.shared.models.flow.parameters.{Parameter, StringParameter, StringSetParameter}
import com.harana.sdk.shared.utils.HMap
import com.harana.ui.components.LinkType
import com.harana.ui.components.widgets.PillChartType

import java.time.Instant

case class GridPageItem(id: String,
                        title: String,
                        description: Option[String],
                        tags: Set[String],
                        created: Instant,
                        updated: Instant,
                        link: Option[LinkType],
                        chartType: Option[PillChartType] = None,
                        entitySubType: Option[String] = None,
                        background: Option[Background] = None,
                        parameterValues: HMap[Parameter.Values] = HMap.empty,
                        additionalData: Map[String, AnyRef] = Map.empty)

object GridPageItem {
  val titleParameter = StringParameter("title")
  val descriptionParameter = StringParameter("description")
  val tagsParameter = StringSetParameter("tags")
}