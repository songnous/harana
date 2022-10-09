package com.harana.sdk.shared.models.flow.actiontypes

import com.harana.sdk.shared.models.common.Version
import com.harana.sdk.shared.models.flow.actionobjects.RowsFiltererInfo
import com.harana.sdk.shared.models.flow.catalogs.ActionCategory.Filtering
import com.harana.sdk.shared.models.flow.documentation.ActionDocumentation
import com.harana.sdk.shared.models.flow.utils.Id

import izumi.reflect.Tag

trait FilterRowsInfo
  extends TransformerAsActionInfo[RowsFiltererInfo]
    with ActionDocumentation {

  val id: Id = "7d7eddfa-c9be-48c3-bb8c-5f7cc59b403a"
  val name = "filter-rows"
  val since = Version(1, 0, 0)
  val category = Filtering

  lazy val portO_1: Tag[RowsFiltererInfo] = typeTag

}

object FilterRowsInfo extends FilterRowsInfo