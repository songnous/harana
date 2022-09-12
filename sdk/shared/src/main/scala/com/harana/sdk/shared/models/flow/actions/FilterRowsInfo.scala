package com.harana.sdk.shared.models.flow.actions

import com.harana.sdk.shared.models.common.Version
import com.harana.sdk.shared.models.flow.catalogs.ActionCategory.Filtering
import com.harana.sdk.shared.models.flow.actionobjects.RowsFiltererInfo
import com.harana.sdk.shared.models.flow.catalogs.ActionCategory.Filtering
import com.harana.sdk.shared.models.flow.documentation.ActionDocumentation
import com.harana.sdk.shared.models.flow.utils.Id

import scala.reflect.runtime.universe.{TypeTag, typeTag}

trait FilterRowsInfo
  extends TransformerAsActionInfo[RowsFiltererInfo]
    with ActionDocumentation {

  val id: Id = "7d7eddfa-c9be-48c3-bb8c-5f7cc59b403a"
  val name = "Filter Rows"
  val description = "Creates a DataFrame containing only rows satisfying given condition"
  val since = Version(1, 0, 0)
  val category = Filtering

  lazy val portO_1: TypeTag[RowsFiltererInfo] = typeTag

}

object FilterRowsInfo extends FilterRowsInfo