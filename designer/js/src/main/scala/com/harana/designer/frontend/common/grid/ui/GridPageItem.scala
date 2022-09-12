
package com.harana.designer.frontend.common.grid.ui

import java.time.Instant

import com.harana.sdk.shared.models.common.Parameter.ParameterName
import com.harana.sdk.shared.models.common.{Background, ParameterValue}
import com.harana.ui.components.LinkType
import com.harana.ui.components.widgets.PillChartType

case class GridPageItem(id: String,
                        title: String,
                        description: Option[String],
                        tags: Set[String],
                        created: Instant,
                        updated: Instant,
                        chartType: Option[PillChartType],
                        link: LinkType,
                        entitySubType: Option[String],
                        background: Option[Background],
                        parameterValues: Map[ParameterName, ParameterValue],
                        additionalData: Map[String, AnyRef] = Map())